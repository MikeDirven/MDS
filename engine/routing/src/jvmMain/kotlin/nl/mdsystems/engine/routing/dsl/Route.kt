package nl.mdsystems.engine.routing.dsl

import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.pipelines.enums.HttpMethod
import nl.mdsystems.engine.routing.MdsEngineRouting
import nl.mdsystems.engine.routing.enums.RouteContextState
import nl.mdsystems.engine.routing.interfaces.RouteBuilder
import nl.mdsystems.engine.routing.types.Route
import nl.mdsystems.engine.routing.types.RouteHandler
import kotlin.reflect.KProperty0

fun RouteBuilder.route(
    path: String,
    builder: RouteBuilder.() -> Unit
) {
    object : RouteBuilder() {
        override val routingEngine: MdsEngineRouting
            get() = this@route.routingEngine

        override val socketContext: KProperty0<HttpServer>
            get() = this@route.socketContext

        override val parentPath: String
            get() = "${this@route.parentPath}/${path.trim('/')}"
    }.apply(builder).createMethods()
}

fun RouteBuilder.get(
    path: String = "",
    handler: RouteHandler
){
    this.getRoute = Route(
        HttpMethod.GET,
        "$parentPath/${path.trim('/')}",
        handler
    )
}

fun RouteBuilder.post(
    path: String = "",
    handler: RouteHandler
){
    this.postRoute = Route(
        HttpMethod.POST,
        "$parentPath/${path.trim('/')}",
        handler
    )
}

fun RouteBuilder.put(
    path: String = "",
    handler: RouteHandler
){
    this.putRoute = Route(
        HttpMethod.PUT,
        "$parentPath/${path.trim('/')}",
        handler
    )
}

fun RouteBuilder.delete(
    path: String = "",
    handler: RouteHandler
){
    this.deleteRoute = Route(
        HttpMethod.DELETE,
        "$parentPath/${path.trim('/')}",
        handler
    )
}

fun RouteBuilder.patch(
    path: String = "",
    handler: RouteHandler
){
    this.patchRoute = Route(
        HttpMethod.PATCH,
        "$parentPath/${path.trim('/')}",
        handler
    )
}