package nl.mdsystems.engine

import nl.mdsystems.engine.core.MdsEngine
import nl.mdsystems.engine.core.interfaces.MdsEngineConfig
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.metrics.MdsEngineMetrics
import nl.mdsystems.engine.modules.MdsEngineModules
import nl.mdsystems.engine.routing.MdsEngineRouting
import nl.mdsystems.engine.socket.MdsEngineHttpSocket
import nl.mdsystems.engine.threading.MdsEngineThreading
import nl.mdsystems.utils.setStartingTime
import kotlin.system.exitProcess

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
class MdsEngineMain(
    config: MdsEngineConfig.() -> Unit
) : MdsEngine(config) {
    internal lateinit var logging: MdsEngineLogging
    internal lateinit var threading: MdsEngineThreading
    internal lateinit var serverSocket: MdsEngineHttpSocket
    internal lateinit var routing: MdsEngineRouting
    internal var modules: MdsEngineModules? = null
    internal var metrics: MdsEngineMetrics? = null

    override fun start() {
        logging = engineConfig.getComponent(MdsEngineLogging.COMPONENT)
        metrics = engineConfig.getComponentOrNull(MdsEngineMetrics.COMPONENT)
        threading = engineConfig.getComponent(MdsEngineThreading.COMPONENT)
        serverSocket = engineConfig.getComponent(MdsEngineHttpSocket.COMPONENT)
        routing = engineConfig.getComponent(MdsEngineRouting.COMPONENT)
        modules = engineConfig.getComponentOrNull(MdsEngineModules.COMPONENT)

        setStartingTime()

        serverSocket.setRequestExecutor(threading::selectLeastBusyPool)

        // Startup modules loader
        modules?.run {
            loadModules()
        }

        // Log startup message
        logging.info("Starting MDS Engine...")

        super.start()
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
}