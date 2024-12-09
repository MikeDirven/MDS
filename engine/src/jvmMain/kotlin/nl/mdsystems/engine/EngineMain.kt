package nl.mdsystems.engine

import nl.mdsystems.engine.extensions.setStartingTime
import nl.mdsystems.engine.interfaces.EngineMainConfig
import nl.mdsystems.engine.interfaces.EngineModulesConfig
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.logging.interfaces.EngineLoggingConfig
import nl.mdsystems.engine.metrics.MdsEngineMetrics
import nl.mdsystems.engine.modules.MdsEngineModules
import nl.mdsystems.engine.routing.MdsEngineRouting
import nl.mdsystems.engine.routing.interfaces.EngineRoutingConfig
import nl.mdsystems.engine.socket.EngineHttpSocket
import nl.mdsystems.engine.socket.interfaces.EngineSocketConfig
import nl.mdsystems.engine.threading.MdsEngineThreading
import nl.mdsystems.engine.threading.interfaces.EngineThreadPoolConfiguration
import kotlin.system.exitProcess

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class EngineMain private constructor(
    config: EngineMainConfig
) : Thread("Mds_Engine_Thread") {
    internal val logging = MdsEngineLogging(config.logging)
    internal val serverSocket = EngineHttpSocket(config.socket)
    internal val threading = MdsEngineThreading(config.threading)
    internal val routing = MdsEngineRouting(serverSocket::socket, config.routing)
    internal val modules = MdsEngineModules(config.modules)
    internal val metrics = MdsEngineMetrics()

    init {
        setStartingTime()

        serverSocket.setRequestExecutor(threading::selectLeastBusyPool)

        // Startup modules loader
        modules.run {
            loadModules()
        }

        // Log startup message
        logging.info("Starting MDS Engine...")
    }


    override fun run() {
        serverSocket.startServer()

        shutDown()
    }

    private fun shutDown() {
        serverSocket.stopServer()

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
                    override var modules: (EngineModulesConfig.() -> Unit)? = null
                }.apply {
                    config?.invoke(this)
                }
            ).start()
        }
    }
}