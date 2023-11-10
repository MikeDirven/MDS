package mds.plugins.serialization.extensions

import mds.engine.classes.ContentType
import mds.engine.classes.HttpCall
import mds.engine.classes.HttpRequest
import mds.engine.plugins.extensions.pluginOrNull
import mds.plugins.serialization.ContentNegotiation
import java.lang.RuntimeException

inline fun <reified T : Any> HttpCall.receive() : T = application.pluginOrNull(ContentNegotiation).let { contentNegotiation ->
    val contentType = ContentType.parse(
        request.headers.get("Content-Type") ?: throw RuntimeException("No content type found")
    )
    val serializer = contentNegotiation?.serializers?.get(contentType)
        ?: contentNegotiation?.serializers?.entries?.firstOrNull()?.value ?: throw RuntimeException("No serializer found for ${contentType.actual}")


    request.body = serializer.deSerializeFromString(request.body as String, T::class.java)
    request.body as T
}