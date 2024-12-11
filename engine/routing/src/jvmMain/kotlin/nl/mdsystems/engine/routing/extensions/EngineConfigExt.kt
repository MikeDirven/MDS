package nl.mdsystems.engine.routing.extensions

import nl.mdsystems.engine.core.interfaces.MdsEngineConfig
import nl.mdsystems.engine.routing.MdsEngineRouting
import nl.mdsystems.engine.routing.interfaces.RoutingBuilder


fun MdsEngineConfig.routing(configure: RoutingBuilder.() -> Unit) {
    getComponentOrNull(MdsEngineRouting.COMPONENT)?.apply {
        configure(getBuilder())
    } ?: registerComponent(
            MdsEngineRouting.COMPONENT,
            MdsEngineRouting(configure)
        )
}