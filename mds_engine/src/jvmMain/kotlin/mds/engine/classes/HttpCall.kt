package mds.engine.classes

import mds.engine.handlers.Application

data class HttpCall(
    val application: Application,
    val request: HttpRequest,
    val response: HttpResponse
)
