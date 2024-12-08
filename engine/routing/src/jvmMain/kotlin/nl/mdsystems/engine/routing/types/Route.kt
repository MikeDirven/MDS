package nl.mdsystems.engine.routing.types

import nl.mdsystems.engine.pipelines.enums.HttpMethod
import nl.mdsystems.engine.routing.enums.RouteContextState

class Route(
    val method: HttpMethod = HttpMethod.GET,
    val path: String,
    val handler: RouteHandler
) {
    internal var state: RouteContextState = RouteContextState.CREATED
}