package mds.engine

import mds.engine.classes.ContentType
import mds.engine.classes.HttpException
import mds.engine.classes.HttpResponse
import mds.engine.enums.Hook
import mds.engine.enums.HttpStatusCode
import mds.engine.handlers.Application
import mds.engine.interfaces.ExceptionHandler
import mds.engine.interfaces.MdsEngineConfigBuilder
import mds.engine.interfaces.MdsEngineExceptions
import mds.engine.interfaces.MdsEngineThreading
import mds.engine.logging.Tags
import java.net.InetAddress
import java.net.ServerSocket
import java.util.concurrent.ThreadPoolExecutor
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName
import kotlin.system.exitProcess

class MdsEngine(
    configuration: (MdsEngineConfigBuilder.() -> Unit)? = null
) : Application(),
    MdsEngineConfigBuilder,
    MdsEngineThreading
{
    override var port: Int = 0
    override var host: String = "localhost"
    override var maxPools: Int = 2
    override var maxPoolThreads: Int = 4
    override lateinit var entryPoint: mds.engine.MdsEngine.() -> Unit
    override lateinit var threadPools: List<ThreadPoolExecutor>
    override val exceptionsToCatch: MutableMap<KClass<Exception>, ExceptionHandler> = mutableMapOf(
        Exception::class to { call, cause ->
            HttpResponse(
                HttpStatusCode.INTERNAL_SERVER_ERROR,
                ContentType.Application.JSON,
                HttpException(
                    cause::class.jvmName,
                    cause.message ?: "Unknown exception"
                )
            )
        }
    )

    private var server: ServerSocket

    init {
        //Before starting hook
        applicationHooks.filter { it.hook == Hook.BEFORE_STARTING }.forEach {
            it.function(this, null, null)
        }

        // Apply builder configuration
        configuration?.let { this.apply(it) }

        // Create thread pools
        createThreadPoolList()

        // Execute entry point
        this.entryPoint()

        // Get system environment overrides
        System.getenv().apply {
            get("HOST")?.let { host = it }
            get("PORT")?.let { port = it.toInt() }
        }

        // Configure socket
        server = ServerSocket(port,1, InetAddress.getByName(host))
    }

    override fun run() {
        //Engine started hook
        applicationHooks.filter { it.hook == Hook.SERVER_STARTED }.forEach {
            it.function(this, null, null)
        }
        println("${Tags.engine}Listening on: ${server.inetAddress}:${server.localPort}")

        while (!interrupted()){
            val clientSocket = server.accept()
            threadPools.random().submit {
                handleIncomingPipeline(clientSocket)
            }
        }

        shutDown()
    }

    private fun shutDown(){
        //Shutting down hook
        applicationHooks.filter { it.hook == Hook.SHUTDOWN }.forEach {
            it.function(this, null, null)
        }
        exitProcess(0)
    }
}

