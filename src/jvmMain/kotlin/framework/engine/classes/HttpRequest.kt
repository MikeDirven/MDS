package framework.engine.classes

import framework.engine.enums.HttpStatusCode
import framework.engine.enums.RequestMethods
import framework.engine.handlers.Application

data class HttpRequest(
    val application: Application,
    val protocol: String,
    val method: RequestMethods,
    val path: String,
    val queryParams: Map<String, String>,
    var headers: Map<String,String> = mapOf(),
    var body: Any = ""
){
    fun bodyAsText() = this.body
    fun respond(statusCode: HttpStatusCode, contentType: ContentType, response: String) = HttpResponse(statusCode, contentType, response)
}
