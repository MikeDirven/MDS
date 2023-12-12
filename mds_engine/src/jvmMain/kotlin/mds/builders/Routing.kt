package mds.builders

import mds.engine.classes.*
import mds.engine.enums.RequestMethods
import mds.engine.handlers.Application
import mds.engine.handlers.Routing
import mds.engine.pipelines.subPipelines.ResponsePipeline

fun Application.routing(builder: Routing.() -> Unit) = this.routing.apply(builder)

fun Routing.route(path: String = "", block: Route.() -> Unit) = routes.add(
    Route(
        application,
        path
    ).apply(block)
)

fun Route.route(path: String = "", block: Route.() -> Route) = Route(
    application,
    this.path.trimEnd('/').plus('/').plus(path.trimStart('/'))
)

fun Routing.get(path: String = "", block: (call: ResponsePipeline) -> HttpResponse) = routes.firstOrNull {
    it.path == path
}?.apply {
    requestHandler.add(RequestHandler(path, RequestMethods.GET, block))
} ?: Route(
    application,
    path,
    mutableListOf(RequestHandler(path, RequestMethods.GET, block))
).apply {
    routes.add(this)
}

fun Routing.post(path: String = "", block: (call: ResponsePipeline) -> HttpResponse) = routes.firstOrNull {
    it.path == path
}?.apply {
    requestHandler.add(RequestHandler(path, RequestMethods.POST, block))
} ?: Route(
    application,
    path,
    mutableListOf(RequestHandler(path, RequestMethods.POST, block))
).apply {
    routes.add(this)
}

fun Routing.put(path: String = "", block: (call: ResponsePipeline) -> HttpResponse) = routes.firstOrNull {
    it.path == path
}?.apply {
    requestHandler.add(RequestHandler(path, RequestMethods.PUT, block))
} ?: Route(
    application,
    path,
    mutableListOf(RequestHandler(path, RequestMethods.PUT, block))
).apply {
    routes.add(this)
}

fun Routing.patch(path: String = "", block: (call: ResponsePipeline) -> HttpResponse) = routes.firstOrNull {
    it.path == path
}?.apply {
    requestHandler.add(RequestHandler(path, RequestMethods.PATCH, block))
} ?: Route(
    application,
    path,
    mutableListOf(RequestHandler(path, RequestMethods.PATCH, block))
).apply {
    routes.add(this)
}

fun Route.get(path: String = "", block: (call: ResponsePipeline) -> HttpResponse) = this.apply {
    this.requestHandler.add(
        RequestHandler(
            path = this.path.trimEnd('/').plus('/').plus(path.trimStart('/')),
            RequestMethods.GET,
            block
        )
    )
}

fun Route.post(path: String = "", block: (call: ResponsePipeline) -> HttpResponse) = requestHandler.add(
        RequestHandler(
            path = this.path.trimEnd('/').plus('/').plus(path.trimStart('/')),
            RequestMethods.GET,
            block
        )
    )

fun Route.put(path: String = "", block: (call: ResponsePipeline) -> HttpResponse) = requestHandler.add(
        RequestHandler(
            path = this.path.trimEnd('/').plus('/').plus(path.trimStart('/')),
            RequestMethods.GET,
            block
        )
    )

fun Route.patch(path: String = "", block: (call: ResponsePipeline) -> HttpResponse) = requestHandler.add(
        RequestHandler(
            path = this.path.trimEnd('/').plus('/').plus(path.trimStart('/')),
            RequestMethods.GET,
            block
        )
    )