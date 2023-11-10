package mds.plugins.serialization.extensions

import mds.engine.classes.*
import mds.engine.enums.HttpStatusCode

fun HttpCall.respond(body: Any) : HttpResponse {
    val contentType = ContentType.parse(
        request.headers.get("Content-Type") ?: throw RuntimeException("No content type found")
    )

    return response.apply {
        status = HttpStatusCode.OK
        headers.add(HttpHeader("content-type", contentType.actual))
        response = body
    }
}

fun HttpCall.respond(statusCode: HttpStatusCode, body: Any) : HttpResponse {
    val contentType = ContentType.parse(
        request.headers.get("Content-Type") ?: throw RuntimeException("No content type found")
    )

    return response.apply {
        status = statusCode
        headers.add(HttpHeader("content-type", contentType.actual))
        response = body
    }
}

fun HttpCall.respond(contentType: ContentType, body: Any) = response.apply {
    status = HttpStatusCode.OK
    headers.add(HttpHeader("content-type", contentType.actual))
    response = body
}

fun HttpCall.respond(statusCode: HttpStatusCode, contentType: ContentType, body: Any) = response.apply {
    status = statusCode
    headers.add(HttpHeader("content-type", contentType.actual))
    response = body
}