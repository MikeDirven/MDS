package mds.engine.classes

import mds.engine.enums.HttpStatusCode

class HttpResponse(
    var status: HttpStatusCode = HttpStatusCode.OK,
    var contentType: ContentType = ContentType.Text.plain,
    var headers: MutableList<HttpHeader> = mutableListOf(),
    var response: Any? = null
) {
    internal fun toResponseString(): String = "HTTP/1.1 ${status.code} ${status.name}\r\n${headers.joinToString(separator = "\r\n") { "${it.header}: ${it.value}" }}\r\n\r\n$response"
}