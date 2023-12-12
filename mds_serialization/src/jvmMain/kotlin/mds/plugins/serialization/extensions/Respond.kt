package mds.plugins.serialization.extensions

import mds.engine.classes.*
import mds.engine.enums.HttpStatusCode
import mds.engine.pipelines.subPipelines.ResponsePipeline

fun ResponsePipeline.respond(body: Any) : HttpResponse {
    val contentType = ContentType.parse(
        request.headers.get("Content-Type") ?: throw RuntimeException("No content type found")
    )

    return response.apply {
        status = HttpStatusCode.OK
        headers.add(HttpHeader("content-type", contentType.actual))
        response = body
    }
}

fun ResponsePipeline.respond(statusCode: HttpStatusCode, body: Any) : HttpResponse {
    val contentType = ContentType.parse(
        request.headers.get("Content-Type") ?: throw RuntimeException("No content type found")
    )

    return response.apply {
        status = statusCode
        headers.add(HttpHeader("content-type", contentType.actual))
        response = body
    }
}

fun ResponsePipeline.respond(contentType: ContentType, body: Any) = response.apply {
    status = HttpStatusCode.OK
    headers.add(HttpHeader("content-type", contentType.actual))
    response = body
}

fun ResponsePipeline.respond(statusCode: HttpStatusCode, contentType: ContentType, body: Any) = response.apply {
    status = statusCode
    headers.add(HttpHeader("content-type", contentType.actual))
    response = body
}