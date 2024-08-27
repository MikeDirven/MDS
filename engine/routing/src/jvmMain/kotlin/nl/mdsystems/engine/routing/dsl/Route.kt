package nl.mdsystems.engine.routing.dsl

import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.pipelines.ResponsePipeline
import nl.mdsystems.engine.pipelines.enums.HttpMethod
import nl.mdsystems.engine.routing.interfaces.RouteBuilder
import nl.mdsystems.engine.routing.types.RouteHandler
import kotlin.reflect.KProperty0

fun RouteBuilder.route(
    path: String,
    builder: RouteBuilder.() -> Unit
) {
    object : RouteBuilder() {
        override val socketContext: KProperty0<HttpServer>
            = this@route.socketContext

        override val logging: KProperty0<MdsEngineLogging>
            = this@route.logging

        override val parentPath: String
            = "${this@route.parentPath}/${path.trim('/')}"


        // handlers
        override var getHandler: RouteHandler? = null
        override var postHandler: RouteHandler? = null
        override var putHandler: RouteHandler? = null
        override var deleteHandler: RouteHandler? = null
        override var patchHandler: RouteHandler? = null
    }.apply(builder).createMethods()
}

fun RouteBuilder.get(
    path: String,
    handler: RouteHandler
) {
    socketContext.get()
        .createContext(path)
        .apply {
            setHandler{ connection ->
                ResponsePipeline(connection, logging).also { pipeline ->
                    if(pipeline.request.method == HttpMethod.GET)
                        handler(pipeline)
                    else
                        connection.sendResponseHeaders(405, 0)
                }
                connection.close()
            }

            // TODO Authentication
        }
}

fun RouteBuilder.get(
    handler: RouteHandler
){
    this.getHandler = handler
}

fun RouteBuilder.post(
    path: String,
    handler: RouteHandler
) {
    socketContext.get()
        .createContext(path)
        .apply {
            setHandler{ connection ->
                ResponsePipeline(connection, logging).also { pipeline ->
                    if(pipeline.request.method == HttpMethod.POST)
                        handler(pipeline)
                    else
                        connection.sendResponseHeaders(405, 0)
                }
                connection.close()
            }

            // TODO Authentication
        }
}