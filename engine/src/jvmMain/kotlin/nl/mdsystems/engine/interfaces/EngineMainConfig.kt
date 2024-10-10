package nl.mdsystems.engine.interfaces

import nl.mdsystems.engine.EngineMain
import nl.mdsystems.engine.logging.interfaces.EngineLoggingConfig
import nl.mdsystems.engine.routing.interfaces.EngineRoutingConfig
import nl.mdsystems.engine.socket.interfaces.EngineSocketConfig
import nl.mdsystems.engine.threading.interfaces.EngineThreadPoolConfiguration
import nl.mdsystems.engine.types.MdsModule

interface EngineMainConfig {
    var logging: (EngineLoggingConfig.() -> Unit)?
    var threading: (EngineThreadPoolConfiguration.() -> Unit)?
    var socket: (EngineSocketConfig.() -> Unit)?
    var routing: (EngineRoutingConfig.() -> Unit)?
    var modules: List<MdsModule>

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

    fun modules(vararg modulesToAdd: MdsModule) {
        modules = buildList {
            addAll(modules)
            addAll(modulesToAdd)
        }
    }

    fun modules(modulesToAdd: List<MdsModule>) {
        modules = buildList {
            addAll(modules)
            addAll(modulesToAdd)
        }
    }

    fun addModule(module: MdsModule) {
        modules = buildList {
            addAll(modules)
            add(module)
        }
    }

    operator fun List<MdsModule>.plus(module: MdsModule){
        modules = buildList {
            addAll(modules)
            add(module)
        }
    }

    infix fun List<MdsModule>.add(module: MdsModule){
        modules = buildList {
            addAll(modules)
            add(module)
        }
    }
}