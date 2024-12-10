package mds.plugins.serialization

import mds.engine.classes.ContentType
import mds.engine.classes.HttpResponse
import mds.engine.enums.Hook
import mds.engine.handlers.Application
import mds.engine.plugins.MdsPlugin
import mds.engine.plugins.PluginKey
import mds.plugins.serialization.interfaces.Serializer
import mds.plugins.serialization.interfaces.ContentNegotiationConfig
import java.lang.RuntimeException

class ContentNegotiation(
    private val application: Application
) : ContentNegotiationConfig {
    override val serializers: MutableMap<ContentType, Serializer<*>> = mutableMapOf()

    fun configureHooks() {
        application.on(Hook.RESPONSE_READY){call, response ->
            response?.let { responseClass ->
                val serializer = serializers.get(responseClass.contentType)
                    ?: serializers.entries.firstOrNull()?.value ?: throw RuntimeException("No serializer found for $responseClass.contentType")

                responseClass.response?.let { responseBody ->
                    responseClass.response = serializer.serializeToString(responseBody)
                }
            }
        }
    }

    companion object : MdsPlugin<ContentNegotiationConfig, ContentNegotiation> {
        override val key: PluginKey<ContentNegotiation> = PluginKey("ContentNegotiation")

        override fun install(application: Application, configure: ContentNegotiationConfig.() -> Unit): ContentNegotiation {
            return ContentNegotiation(application).apply{
                configure()
                configureHooks()
            }
        }
    }
}