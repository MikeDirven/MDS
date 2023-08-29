package framework.engine.serialization.interfaces

import framework.engine.classes.ContentType

interface Serializer<T> {
    var internalContentType: ContentType
    var actualSerializerClass: T

    fun serializeToString(data: Any) : Any

    fun <T : Any> deSerializeFromString(data: String, outputType: Class<T>) : T
}