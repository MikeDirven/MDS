package nl.mdsystems.engine

import nl.mdsystems.engine.core.MdsEngine
import nl.mdsystems.engine.core.interfaces.MdsEngineConfig
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.MdsEngineLogging.Companion.getValue
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.metrics.MdsEngineMetrics
import nl.mdsystems.engine.modules.MdsEngineModules
import nl.mdsystems.engine.routing.MdsEngineRouting
import nl.mdsystems.engine.socket.MdsEngineHttpSocket
import nl.mdsystems.engine.socket.MdsEngineHttpSocket.Companion.getValue
import nl.mdsystems.engine.threading.MdsEngineThreading
import nl.mdsystems.engine.threading.MdsEngineThreading.Companion.getValue
import nl.mdsystems.utils.setStartingTime
import kotlin.system.exitProcess

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
class MdsEngineMain(
    config: MdsEngineConfig.() -> Unit
) : MdsEngine(config) {
    internal val logging: MdsEngineLogging by MdsEngineLogging
    internal val threading: MdsEngineThreading by MdsEngineThreading
    internal val serverSocket: MdsEngineHttpSocket by MdsEngineHttpSocket
    internal val routing: MdsEngineRouting = engineConfig.getComponent(MdsEngineRouting.COMPONENT)
    internal val modules: MdsEngineModules? = engineConfig.getComponentOrNull(MdsEngineModules.COMPONENT)
    internal val metrics: MdsEngineMetrics? = engineConfig.getComponentOrNull(MdsEngineMetrics.COMPONENT)

    override fun start() {
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