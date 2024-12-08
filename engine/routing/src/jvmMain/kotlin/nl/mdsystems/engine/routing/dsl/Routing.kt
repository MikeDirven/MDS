package nl.mdsystems.engine.routing.dsl

import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.routing.MdsEngineRouting
import nl.mdsystems.engine.routing.interfaces.RouteBuilder
import nl.mdsystems.engine.routing.interfaces.RoutingBuilder
import nl.mdsystems.engine.routing.types.Route
import nl.mdsystems.engine.routing.types.RouteHandler
import kotlin.reflect.KProperty0

fun RoutingBuilder.route(
    path: String,
    builder: RouteBuilder.() -> Unit
) {
    object : RouteBuilder() {
        override val routingEngine: MdsEngineRouting
            get() = this@route.routingEngine

        override val socketContext: KProperty0<HttpServer>
            get() = this@route.socketContext

        override val parentPath: String
            get() = "$rootPath/${path.trim('/')}"
    }.apply(builder).createMethods()
}