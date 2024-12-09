package nl.mdsystems.engine.routing.types

import com.sun.net.httpserver.HttpContext
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import nl.mdsystems.engine.metrics.MdsEngineMetrics
import nl.mdsystems.engine.pipelines.ResponsePipeline
import nl.mdsystems.engine.pipelines.enums.HttpMethod
import nl.mdsystems.engine.routing.enums.RouteContextState
import java.util.concurrent.ConcurrentHashMap

class RouteContext(
    val path: String,
    var context: HttpContext
) : HttpHandler, Comparable<RouteContext> {
    internal val metrics by MdsEngineMetrics
    internal var state: RouteContextState = RouteContextState.INITIALIZED
    internal val handlers: ConcurrentHashMap<HttpMethod, RouteHandler> = ConcurrentHashMap()

    override fun compareTo(other: RouteContext): Int {
        return when{
            this.hashCode() > other.hashCode() -> 1
            this.hashCode() < other.hashCode() -> -1
            else -> 0
        }
    }

    override fun handle(exchange: HttpExchange) {
        val startProcessingTime = System.currentTimeMillis()

        ResponsePipeline(exchange).also { pipeline ->
            when(pipeline.request.method){
                HttpMethod.GET -> handlers[HttpMethod.GET]?.invoke(pipeline)
                HttpMethod.POST -> handlers[HttpMethod.POST]?.invoke(pipeline)
                HttpMethod.PUT -> handlers[HttpMethod.PUT]?.invoke(pipeline)
                HttpMethod.DELETE -> handlers[HttpMethod.DELETE]?.invoke(pipeline)
                HttpMethod.PATCH -> handlers[HttpMethod.PATCH]?.invoke(pipeline)
                else -> {
                    pipeline.connection.sendResponseHeaders(404, 0)
                }
            }

            metrics.updateRequestProcessingTimes(
                System.currentTimeMillis() - startProcessingTime
            )

            pipeline.connection.close()
        }
    }

    init {
        context.handler = this
    }

    fun addHandler(method: HttpMethod, handler: RouteHandler) : RouteContext{
        handlers[method] = handler
        return this
    }
}