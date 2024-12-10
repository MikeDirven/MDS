package mds.plugins.serialization.interfaces

import mds.engine.classes.ContentType

interface ContentNegotiationConfig {
    val serializers: MutableMap<ContentType, Serializer<*>>
}