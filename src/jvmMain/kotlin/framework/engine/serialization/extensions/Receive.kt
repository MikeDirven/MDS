package framework.engine.serialization.extensions

import framework.engine.classes.ContentType
import framework.engine.classes.HttpRequest
import framework.engine.plugins.extensions.pluginOrNull
import framework.engine.serialization.ContentNegotiation
import java.lang.RuntimeException

inline fun <reified T : Any> HttpRequest.receive() : T = application.pluginOrNull(ContentNegotiation).let { contentNegotiation ->
    val contentType = ContentType.parse(
        headers.get("Content-Type") ?: throw RuntimeException("No content type found")
    )
    val serializer = contentNegotiation.serializers.get(contentType)
        ?: contentNegotiation.serializers.entries.firstOrNull()?.value ?: throw RuntimeException("No serializer found for ${contentType.actual}")


    body = serializer.deSerializeFromString(body as String, T::class.java)
    body as T
}