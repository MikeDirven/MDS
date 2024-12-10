package nl.mdsystems.engine.metrics.extensions

import nl.mdsystems.engine.core.interfaces.MdsEngineConfig
import nl.mdsystems.engine.metrics.MdsEngineMetrics

fun MdsEngineConfig.metrics(){
    val instance = MdsEngineMetrics()
    registerComponent(MdsEngineMetrics.COMPONENT, instance)
}