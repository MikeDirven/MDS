package nl.mdsystems.engine.routing.interfaces

import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.routing.MdsEngineRouting
import org.jetbrains.annotations.ApiStatus.Internal
import kotlin.reflect.KProperty0

abstract class RoutingBuilder(
    internal val routingEngine: MdsEngineRouting,
    internal val socketContext: KProperty0<HttpServer>,
    internal val logging: MdsEngineLogging
) : EngineRoutingConfig