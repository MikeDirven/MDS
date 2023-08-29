package framework.engine.serialization.extensions

import framework.engine.classes.ContentType
import framework.engine.classes.HttpRequest
import framework.engine.classes.HttpResponse
import framework.engine.enums.HttpStatusCode
import framework.engine.plugins.extensions.pluginOrNull
import framework.engine.serialization.ContentNegotiation
import java.lang.RuntimeException

fun HttpRequest.respond(body: Any) = application.pluginOrNull(ContentNegotiation).let { contentNegotiation ->
    val contentType = ContentType.parse(
        headers.get("Content-Type") ?: throw RuntimeException("No content type found")
    )
    val serializer = contentNegotiation.serializers.get(contentType)
        ?: contentNegotiation.serializers.entries.firstOrNull()?.value ?: throw RuntimeException("No serializer found for $contentType")

    HttpResponse(HttpStatusCode.OK, contentType, serializer.serializeToString(body))
}

fun HttpRequest.respond(statusCode: HttpStatusCode, body: Any) = application.pluginOrNull(ContentNegotiation).let { contentNegotiation ->
    val contentType = ContentType.parse(
        headers.get("Content-Type") ?: throw RuntimeException("No content type found")
    )
    val serializer = contentNegotiation.serializers.get(contentType)
        ?: contentNegotiation.serializers.entries.firstOrNull()?.value ?: throw RuntimeException("No serializer found for $contentType")

    HttpResponse(statusCode, contentType, serializer.serializeToString(body))
}

fun HttpRequest.respond(contentType: ContentType, body: Any) = application.pluginOrNull(ContentNegotiation).let { contentNegotiation ->
    val serializer = contentNegotiation.serializers.get(contentType)
        ?: contentNegotiation.serializers.entries.firstOrNull()?.value ?: throw RuntimeException("No serializer found for $contentType")

    HttpResponse(HttpStatusCode.OK, contentType, serializer.serializeToString(body))
}

fun HttpRequest.respond(statusCode: HttpStatusCode, contentType: ContentType, body: Any) = application.pluginOrNull(ContentNegotiation).let { contentNegotiation ->
    val serializer = contentNegotiation.serializers.get(contentType)
        ?: contentNegotiation.serializers.entries.firstOrNull()?.value ?: throw RuntimeException("No serializer found for $contentType")

    HttpResponse(statusCode, contentType, serializer.serializeToString(body))
}