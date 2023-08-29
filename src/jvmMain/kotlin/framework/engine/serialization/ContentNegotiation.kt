package framework.engine.serialization

import framework.engine.classes.ContentType
import framework.engine.handlers.Application
import framework.engine.plugins.MdsPlugin
import framework.engine.plugins.PluginKey
import framework.engine.serialization.interfaces.Serializer
import framework.engine.serialization.interfaces.ContentNegotiationConfig

class ContentNegotiation(
    val application: Application
) : ContentNegotiationConfig {
    override val serializers: MutableMap<ContentType, Serializer<*>> = mutableMapOf()

    companion object : MdsPlugin<ContentNegotiationConfig, ContentNegotiation> {
        override val key: PluginKey<ContentNegotiation> = PluginKey("ContentNegotiation")

        override fun install(application: Application, configure: ContentNegotiationConfig.() -> Unit): ContentNegotiation {
            return ContentNegotiation(application).apply(configure)
        }
    }
}