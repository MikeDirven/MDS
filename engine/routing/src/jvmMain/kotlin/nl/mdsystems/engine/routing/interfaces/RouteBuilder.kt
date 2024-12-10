package nl.mdsystems.engine.routing.interfaces

import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.MdsEngineLogging.Companion.getValue
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.routing.MdsEngineRouting
import nl.mdsystems.engine.routing.enums.RouteContextState
import nl.mdsystems.engine.routing.types.Route
import nl.mdsystems.engine.routing.types.RouteContext
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.reflect.KProperty0

abstract class RouteBuilder {
    internal abstract val routingEngine: MdsEngineRouting
    internal abstract val socketContext: KProperty0<HttpServer>
    internal val logging by MdsEngineLogging
    internal val routeContexts: ConcurrentSkipListSet<RouteContext> = ConcurrentSkipListSet()

    abstract val parentPath: String

    private fun RouteContext.discardRouteContext() {
        socketContext.get().removeContext(context)
        state = RouteContextState.DISCARDED
        logging.info("Route ${this.path} discarded due to being replaced with new route.")
    }

    internal fun Route.registerRouteContext() {
        // Check if context already exists
        routeContexts.firstOrNull { route ->
            route.path == this.path && route.state == RouteContextState.INITIALIZED
        }?.updateRouteContext(this) ?: this.newRouteContext()
    }

    internal fun RouteContext.updateRouteContext(newRoute: Route) {
        addHandler(newRoute.method, newRoute.handler)
    }

    internal fun Route.newRouteContext() {
        socketContext.get().createContext(path).also { httpContext ->
            routeContexts.add(
                RouteContext(
                    path = this.path,
                    context = httpContext
                ).addHandler(this.method, this.handler)
            )
            logging.info("${this.method} Route $path created.")
        }
    }
}