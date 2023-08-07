package framework.engine.serialization.gson

import com.google.gson.Gson
import framework.engine.classes.ContentType
import framework.engine.classes.HttpRequest
import framework.engine.classes.HttpResponse
import framework.engine.enums.HttpStatusCode
import framework.engine.handlers.Application
import framework.engine.plugins.MdsPlugin
import framework.engine.plugins.PluginKey

class GsonSerializer(
    val application: Application
) : GsonConfig {
    override var contentType: ContentType = ContentType.Application.json

    companion object : MdsPlugin<GsonSerializer, GsonSerializer> {
        override val key: PluginKey<GsonSerializer> = PluginKey("GsonSerializer")

        override fun install(application: Application, configure: GsonSerializer.() -> Unit): GsonSerializer {
            return GsonSerializer(application).apply(configure)
        }

        fun getContentType(application: Application) = (application.applicationPlugins[PluginKey<GsonSerializer>("GsonSerializer")] as GsonSerializer).contentType
    }
}

fun HttpRequest.respond(body: Any) = HttpResponse(HttpStatusCode.OK, GsonSerializer.getContentType(this.application), Gson().toJson(body))
fun HttpRequest.respond(statusCode: HttpStatusCode, body: Any) = HttpResponse(
    statusCode,
    GsonSerializer.getContentType(this.application),
    Gson().toJson(body)
)
fun HttpRequest.respond(contentType: ContentType, body: Any) = HttpResponse(HttpStatusCode.OK, contentType, Gson().toJson(body))
fun HttpRequest.respond(statusCode: HttpStatusCode, contentType: ContentType, body: Any) = HttpResponse(statusCode, contentType, Gson().toJson(body))

inline fun <reified T : Any> HttpRequest.receive() : T {
    body = Gson().fromJson(body as String, T::class.java)
    return body as T
}