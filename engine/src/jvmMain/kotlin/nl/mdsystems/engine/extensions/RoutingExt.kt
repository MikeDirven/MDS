package nl.mdsystems.engine.extensions

import nl.mdsystems.engine.EngineMain
import nl.mdsystems.engine.routing.interfaces.RoutingBuilder

fun EngineMain.routing(block: RoutingBuilder.() -> Unit)
    = routing.getBuilder().apply(block)