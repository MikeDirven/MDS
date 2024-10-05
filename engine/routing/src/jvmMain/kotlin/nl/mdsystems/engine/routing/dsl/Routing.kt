package nl.mdsystems.engine.routing.dsl

import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.routing.interfaces.RouteBuilder
import nl.mdsystems.engine.routing.interfaces.RoutingBuilder
import nl.mdsystems.engine.routing.types.RouteHandler
import kotlin.reflect.KProperty0

fun RoutingBuilder.route(
    path: String,
    builder: RouteBuilder.() -> Unit
) {
    object : RouteBuilder() {
        override val socketContext: KProperty0<HttpServer>
            = this@route.socketContext

        override val logging: KProperty0<MdsEngineLogging>
            = this@route.logging

        override val parentPath: String
            = "$rootPath/${path.trim('/')}"

        // handlers
        override var getHandler: RouteHandler? = null
        override var postHandler: RouteHandler? = null
        override var putHandler: RouteHandler? = null
        override var deleteHandler: RouteHandler? = null
        override var patchHandler: RouteHandler? = null
    }.apply(builder).createMethods()
}