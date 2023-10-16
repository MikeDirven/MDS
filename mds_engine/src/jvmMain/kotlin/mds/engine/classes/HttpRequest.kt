package mds.engine.classes

import mds.engine.enums.HttpStatusCode
import mds.engine.enums.RequestMethods
import mds.engine.handlers.Application
import java.io.BufferedWriter

data class HttpRequest(
    val application: Application,
    val protocol: String,
    val method: RequestMethods,
    val path: String,
    val queryParams: Map<String, String>,
    var headers: Map<String,String> = emptyMap(),
    var body: Any = "",
    val outPutWriter: BufferedWriter
) {
    fun bodyAsText() = this.body
    fun respond(statusCode: HttpStatusCode, contentType: ContentType, response: String) = HttpResponse(statusCode, contentType, response)
}
