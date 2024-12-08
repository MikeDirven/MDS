package nl.mdsystems.engine.routing.interfaces

import com.sun.net.httpserver.Authenticator
import com.sun.net.httpserver.HttpContext
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.pipelines.ResponsePipeline
import nl.mdsystems.engine.pipelines.enums.HttpMethod
import nl.mdsystems.engine.routing.MdsEngineRouting
import nl.mdsystems.engine.routing.enums.RouteContextState
import nl.mdsystems.engine.routing.types.Route
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.reflect.KProperty0

abstract class RouteBuilder {
    internal abstract val routingEngine: MdsEngineRouting
    internal abstract val socketContext: KProperty0<HttpServer>
    internal val logging by MdsEngineLogging
    internal var discardedRouteContexts: ConcurrentSkipListSet<Route> = ConcurrentSkipListSet()

    abstract val parentPath: String

    private fun Route.addRouteToDiscardedList() {
        discardedRouteContexts.add(this.apply { state = RouteContextState.DISCARDED })
        logging.info("${this.method} Route ${this.path} discarded due to being replaced with new route.")
    }

    var getRoute: Route? = null
        set(value) {
            field?.addRouteToDiscardedList()
            field = value
        }
    var postRoute: Route? = null
        set(value) {
            field?.addRouteToDiscardedList()
            field = value
        }
    var putRoute: Route? = null
        set(value) {
            field?.addRouteToDiscardedList()
            field = value
        }
    var deleteRoute: Route? = null
        set(value) {
            field?.addRouteToDiscardedList()
            field = value
        }
    var patchRoute: Route? = null
        set(value) {
            field?.addRouteToDiscardedList()
            field = value
        }

    internal fun createMethods() : HttpContext {
        return socketContext.get().createContext(parentPath) { connection ->
            ResponsePipeline(connection).also { pipeline ->
                when(pipeline.request.method){
                    HttpMethod.GET -> getRoute?.handler?.invoke(pipeline) ?: connection.sendResponseHeaders(404, 0)
                    HttpMethod.POST -> postRoute?.handler?.invoke(pipeline) ?: connection.sendResponseHeaders(404, 0)
                    HttpMethod.PUT -> putRoute?.handler?.invoke(pipeline) ?: connection.sendResponseHeaders(404, 0)
                    HttpMethod.DELETE -> deleteRoute?.handler?.invoke(pipeline) ?: connection.sendResponseHeaders(404, 0)
                    HttpMethod.PATCH -> patchRoute?.handler?.invoke(pipeline) ?: connection.sendResponseHeaders(404, 0)
                    else -> connection.sendResponseHeaders(405, 0)
                }
                connection.close()
            }
        }
    }
}