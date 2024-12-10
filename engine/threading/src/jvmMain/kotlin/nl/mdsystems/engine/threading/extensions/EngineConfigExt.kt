package nl.mdsystems.engine.threading.extensions

import nl.mdsystems.engine.core.interfaces.MdsEngineConfig
import nl.mdsystems.engine.threading.MdsEngineThreading
import nl.mdsystems.engine.threading.interfaces.EngineThreadPoolConfiguration

fun MdsEngineConfig.threading(configure: EngineThreadPoolConfiguration.() -> Unit) {
    val instance = MdsEngineThreading(configure)
    registerComponent(MdsEngineThreading.COMPONENT, instance)
}