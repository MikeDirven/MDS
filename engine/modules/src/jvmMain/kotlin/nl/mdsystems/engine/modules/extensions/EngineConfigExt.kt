package nl.mdsystems.engine.modules.extensions

import nl.mdsystems.engine.core.interfaces.MdsEngineConfig
import nl.mdsystems.engine.modules.MdsEngineModules
import nl.mdsystems.engine.modules.interfaces.EngineModulesConfig


fun MdsEngineConfig.modules(configure: EngineModulesConfig.() -> Unit) {
    val instance = MdsEngineModules(configure)
    registerComponent(MdsEngineModules.COMPONENT, instance)
}