package nl.mdsystems.engine.routing.interfaces

import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.pipelines.ResponsePipeline
import nl.mdsystems.engine.pipelines.enums.HttpMethod
import nl.mdsystems.engine.routing.types.RouteHandler
import kotlin.reflect.KProperty0

abstract class RouteBuilder {
    internal abstract val socketContext: KProperty0<HttpServer>
    internal abstract val logging: KProperty0<MdsEngineLogging>
    abstract val parentPath: String

    abstract var getHandler: RouteHandler?
    abstract var postHandler: RouteHandler?
    abstract var putHandler: RouteHandler?
    abstract var deleteHandler: RouteHandler?
    abstract var patchHandler: RouteHandler?

    internal fun createMethods() {
        socketContext.get().createContext(parentPath) { connection ->
            ResponsePipeline(connection, logging).also { pipeline ->
                when(pipeline.request.method){
                    HttpMethod.GET -> getHandler?.invoke(pipeline) ?: connection.sendResponseHeaders(404, 0)
                    HttpMethod.POST -> postHandler?.invoke(pipeline) ?: connection.sendResponseHeaders(404, 0)
                    HttpMethod.PUT -> putHandler?.invoke(pipeline) ?: connection.sendResponseHeaders(404, 0)
                    HttpMethod.DELETE -> deleteHandler?.invoke(pipeline) ?: connection.sendResponseHeaders(404, 0)
                    HttpMethod.PATCH -> patchHandler?.invoke(pipeline) ?: connection.sendResponseHeaders(404, 0)
                    else -> connection.sendResponseHeaders(405, 0)
                }
                connection.close()
            }
        }
    }
}