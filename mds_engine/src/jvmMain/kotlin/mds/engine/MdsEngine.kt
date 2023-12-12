package mds.engine

import kotlinx.coroutines.CoroutineDispatcher
import mds.engine.logging.extensions.info
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import mds.engine.classes.ContentType
import mds.engine.classes.HttpException
import mds.engine.classes.HttpResponse
import mds.engine.enums.Hook
import mds.engine.enums.HttpStatusCode
import mds.engine.handlers.Application
import mds.engine.interfaces.*
import mds.engine.logging.Tags
import mds.engine.pipelines.ApplicationPipeline
import mds.engine.pipelines.Pipeline
import java.net.InetAddress
import java.net.ServerSocket
import java.text.DecimalFormat
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName
import kotlin.system.exitProcess

class MdsEngine(
    configuration: (MdsEngineConfigBuilder.() -> Unit)? = null
) : Application(),
    MdsEngineConfigBuilder,
    MdsEngineThreading
{
    private val initializeTimeStamp = System.currentTimeMillis()
    val pipeLines: MutableList<Pipeline> = mutableListOf()
    override var port: Int = 0
    override var host: String = "localhost"
    override var maxPools: Int = 2
    override var maxPoolThreads: Int = 4
    override lateinit var entryPoint: mds.engine.MdsEngine.() -> Unit
    override lateinit var threadPools: List<CoroutineDispatcher>
    override val exceptionsToCatch: MutableMap<KClass<Throwable>, ExceptionHandler> = mutableMapOf(
        Throwable::class to { call, cause ->
            HttpResponse(
                HttpStatusCode.INTERNAL_SERVER_ERROR,
                ContentType.Application.JSON,
                mutableListOf(),
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
        server = ServerSocket(port,50, InetAddress.getByName(host))
    }

    fun getElapsedTimeInSecondsWithDecimal(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedTimeMillis = currentTimeMillis - initializeTimeStamp
        val elapsedTimeInSeconds = elapsedTimeMillis / 1000.0

        val decimalFormat = DecimalFormat("0.00")
        return decimalFormat.format(elapsedTimeInSeconds)
    }

    override fun run() {
        //Engine started hook
        applicationHooks.filter { it.hook == Hook.SERVER_STARTED }.forEach {
            it.function(this, null, null)
        }
        info("${Tags.engine} Listening on: ${server.inetAddress}:${server.localPort}")
        info("${Tags.engine} Application started in: ${getElapsedTimeInSecondsWithDecimal()} seconds")

        while (!interrupted()){
            val clientSocket = server.accept()
            threadPools.random().dispatch(EmptyCoroutineContext) {
                    ApplicationPipeline(this, clientSocket){

                    }
            }
        }

        shutDown()
    }

    private fun shutDown(){
        //Shutting down hook
        applicationHooks.filter { it.hook == Hook.SHUTDOWN }.forEach {
            it.function(this, null, null)
        }

        threadPools.forEach {
            it.cancel()
        }

        exitProcess(0)
    }
}

