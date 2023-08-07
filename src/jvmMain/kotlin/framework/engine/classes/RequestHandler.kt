package framework.engine.classes

import framework.engine.enums.RequestMethods

class RequestHandler(
    val path: String,
    val method: RequestMethods,
    val handler: (request: HttpRequest) -> HttpResponse
)