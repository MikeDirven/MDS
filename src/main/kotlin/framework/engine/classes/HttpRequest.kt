package framework.engine.classes

import framework.engine.enums.HttpStatusCode
import framework.engine.enums.RequestMethods

data class HttpRequest(
    val method: RequestMethods,
    val path: String,
    val queryParams: Map<String, String>,
    val protocol: String,
    var body: String = ""
){
    fun bodyAsText() = this.body
    fun respond(statusCode: HttpStatusCode, contentType: ContentType, response: String) = HttpResponse(statusCode, contentType, response)
}
