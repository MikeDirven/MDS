package nl.mdsystems.engine.routing.dsl

import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.pipelines.enums.HttpMethod
import nl.mdsystems.engine.routing.MdsEngineRouting
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
    }.apply(builder)
}

fun RouteBuilder.get(
    path: String = "",
    handler: RouteHandler
){
    Route(
        HttpMethod.GET,
        "$parentPath/${path.trim('/')}/",
        handler
    ).registerRouteContext()
}

fun RouteBuilder.post(
    path: String = "",
    handler: RouteHandler
){
    Route(
        HttpMethod.POST,
        "$parentPath/${path.trim('/')}/",
        handler
    ).registerRouteContext()
}

fun RouteBuilder.put(
    path: String = "",
    handler: RouteHandler
){
    Route(
        HttpMethod.PUT,
        "$parentPath/${path.trim('/')}/",
        handler
    ).registerRouteContext()
}

fun RouteBuilder.delete(
    path: String = "",
    handler: RouteHandler
){
    Route(
        HttpMethod.DELETE,
        "$parentPath/${path.trim('/')}/",
        handler
    ).registerRouteContext()
}

fun RouteBuilder.patch(
    path: String = "",
    handler: RouteHandler
){
    Route(
        HttpMethod.PATCH,
        "$parentPath/${path.trim('/')}/",
        handler
    ).registerRouteContext()
}