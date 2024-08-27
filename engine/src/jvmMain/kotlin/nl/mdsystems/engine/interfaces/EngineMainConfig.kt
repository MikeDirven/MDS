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
}

fun EngineMainConfig.logging(config: EngineLoggingConfig.() -> Unit) {
    logging = config
}

fun EngineMainConfig.threading(config: EngineThreadPoolConfiguration.() -> Unit) {
    threading = config
}

fun EngineMainConfig.socket(config: EngineSocketConfig.() -> Unit) {
    socket = config
}

fun EngineMainConfig.routing(config: EngineRoutingConfig.() -> Unit) {
    routing = config
}

fun EngineMainConfig.modules(vararg modules: MdsModule) {
    this.modules = modules.toList()
}

fun EngineMainConfig.modules(modules: List<MdsModule>) {
    this.modules = modules
}

fun EngineMainConfig.addModule(module: MdsModule) {
    this.modules = buildList {
        addAll(this@addModule.modules)
        add(module)
    }
}