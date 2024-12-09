package nl.mdsystems.engine.routing.types

import nl.mdsystems.engine.pipelines.enums.HttpMethod

data class Route(
    val method: HttpMethod,
    val path: String,
    val handler: RouteHandler
)