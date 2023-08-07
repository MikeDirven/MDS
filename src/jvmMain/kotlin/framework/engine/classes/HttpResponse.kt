package framework.engine.classes

import framework.engine.enums.HttpStatusCode

class HttpResponse(
    val status: HttpStatusCode,
    val contentType: ContentType = ContentType.Text.plain,
    val response: Any? = null
) {
    internal fun toResponseString() =
        "HTTP/1.1 ${status.code} ${status.name}\r\nContent-Type: ${contentType.actual}\r\n\r\n$response"
}