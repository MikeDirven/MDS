package mds.plugins.serialization.extensions

import mds.engine.classes.ContentType
import mds.engine.classes.HttpRequest
import mds.engine.classes.HttpResponse
import mds.engine.enums.HttpStatusCode

fun HttpRequest.respond(body: Any) : HttpResponse {
    val contentType = ContentType.parse(
        headers.get("Content-Type") ?: throw RuntimeException("No content type found")
    )

    return HttpResponse(HttpStatusCode.OK, contentType, body)
}

fun HttpRequest.respond(statusCode: HttpStatusCode, body: Any) : HttpResponse {
    val contentType = ContentType.parse(
        headers.get("Content-Type") ?: throw RuntimeException("No content type found")
    )

    return HttpResponse(statusCode, contentType, body)
}

fun HttpRequest.respond(contentType: ContentType, body: Any) = HttpResponse(HttpStatusCode.OK, contentType, body)

fun HttpRequest.respond(statusCode: HttpStatusCode, contentType: ContentType, body: Any) = HttpResponse(statusCode, contentType, body)