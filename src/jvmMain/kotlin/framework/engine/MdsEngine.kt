package framework.engine

import framework.engine.enums.Hook
import framework.engine.handlers.Application
import framework.engine.interfaces.MdsEngineConfigBuilder
import framework.engine.interfaces.MdsEngineThreading
import framework.engine.logging.Tags
import java.net.InetAddress
import java.net.ServerSocket
import java.util.concurrent.ThreadPoolExecutor
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
    override lateinit var entryPoint: MdsEngine.() -> Unit
    override lateinit var threadPools: List<ThreadPoolExecutor>

    private var server: ServerSocket

    init {
        //Before starting hook
        applicationHooks.filter { it.hook == Hook.BEFORE_STARTING }.forEach {
            it.function(this, null)
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
            it.function(this, null)
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
            it.function(this, null)
        }
        exitProcess(0)
    }
}

