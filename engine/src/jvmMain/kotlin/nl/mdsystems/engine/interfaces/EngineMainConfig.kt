package nl.mdsystems.engine.interfaces

import nl.mdsystems.engine.logging.interfaces.EngineLoggingConfig
import nl.mdsystems.engine.routing.interfaces.EngineRoutingConfig
import nl.mdsystems.engine.socket.interfaces.EngineSocketConfig
import nl.mdsystems.engine.threading.interfaces.EngineThreadPoolConfiguration

actual interface EngineMainConfig {
    var logging: (EngineLoggingConfig.() -> Unit)?
    var threading: (EngineThreadPoolConfiguration.() -> Unit)?
    var socket: (EngineSocketConfig.() -> Unit)?
    var routing: (EngineRoutingConfig.() -> Unit)?
    var modules: (EngineModulesConfig.() -> Unit)?

    fun logging(config: EngineLoggingConfig.() -> Unit) {
        logging = config
    }

    fun threading(config: EngineThreadPoolConfiguration.() -> Unit) {
        threading = config
    }

    fun socket(config: EngineSocketConfig.() -> Unit) {
        socket = config
    }

    fun routing(config: EngineRoutingConfig.() -> Unit) {
        routing = config
    }

    fun modules(config: EngineModulesConfig.() -> Unit) {
        modules = config
    }
}