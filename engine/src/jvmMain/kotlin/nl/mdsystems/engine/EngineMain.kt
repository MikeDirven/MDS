package nl.mdsystems.engine

import nl.mdsystems.engine.extensions.setStartingTime
import nl.mdsystems.engine.interfaces.EngineMainConfig
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.logging.interfaces.EngineLoggingConfig
import nl.mdsystems.engine.metrics.MdsEngineMetrics
import nl.mdsystems.engine.routing.MdsEngineRouting
import nl.mdsystems.engine.routing.interfaces.EngineRoutingConfig
import nl.mdsystems.engine.socket.EngineHttpSocket
import nl.mdsystems.engine.socket.interfaces.EngineSocketConfig
import nl.mdsystems.engine.threading.MdsEngineThreading
import nl.mdsystems.engine.threading.interfaces.EngineThreadPoolConfiguration
import kotlin.system.exitProcess

class EngineMain private constructor(
    config: EngineMainConfig
) : Thread("Mds_Engine_Thread") {
    internal val logging = MdsEngineLogging(config.logging)
    internal val serverSocket = EngineHttpSocket(config.socket)
    internal val threading = MdsEngineThreading(config.threading)
    internal val routing = MdsEngineRouting(serverSocket::socket, ::logging, config.routing)
    internal val metrics = MdsEngineMetrics()

    init {
        setStartingTime()

        serverSocket.run {
            createSocketContext()
            setRequestExecutor(threading::selectLeastBusyPool)
        }

        // initialize modules
        config.modules.forEach { module ->
            module.invoke(this)
        }

        // Log startup message
        logging.info("Starting MDS Engine...")
    }


    override fun run() {
        serverSocket.apply {
            startServer()
        }

        shutDown()
    }

    private fun shutDown() {
        serverSocket.apply {
            stopServer()
        }

        threading.closeAllPools()

        // Log startup done message
        logging.info("MDS Engine Stopped.")

        exitProcess(0)
    }

    companion object {
        operator fun invoke(
            config: (EngineMainConfig.() -> Unit)? = null
        ) = run {
            
            EngineMain(
                object : EngineMainConfig {
                    override var logging: (EngineLoggingConfig.() -> Unit)? = null
                    override var threading: (EngineThreadPoolConfiguration.() -> Unit)? = null
                    override var socket: (EngineSocketConfig.() -> Unit)? = null
                    override var routing: (EngineRoutingConfig.() -> Unit)? = null
                    override var modules: List<EngineMain.() -> Unit> = listOf()
                }.apply {
                    config?.invoke(this)
                }
            ).start()
        }
    }
}