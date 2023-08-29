package framework.engine.serialization.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import framework.engine.classes.ContentType
import framework.engine.serialization.interfaces.Serializer

class Gson(
    config: (GsonBuilder.() -> Unit)?
) : Serializer<Gson> {
    override var internalContentType: ContentType = contentType
    override var actualSerializerClass: Gson = Gson()

    init {
        config?.let { actualSerializerClass = Gson().newBuilder().apply(it).create() }
    }

    override fun serializeToString(data: Any) : String = actualSerializerClass.toJson(data)

    override fun <T: Any> deSerializeFromString(data: String, outputType: Class<T>): T = actualSerializerClass.fromJson(data, outputType)

    companion object {
        val contentType: ContentType = ContentType.Application.JSON
    }
}