package mds.engine.classes

import mds.engine.enums.RequestMethods

class RequestHandler(
    val path: String,
    val method: RequestMethods,
    val handler: (call: HttpCall) -> HttpResponse
)