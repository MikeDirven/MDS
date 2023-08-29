package framework.engine.serialization.interfaces

import framework.engine.classes.ContentType

interface ContentNegotiationConfig {
    val serializers: MutableMap<ContentType, Serializer<*>>
}