package framework.engine.serialization.gson.extensions

import com.google.gson.GsonBuilder
import framework.engine.classes.ContentType
import framework.engine.serialization.gson.Gson
import framework.engine.serialization.interfaces.ContentNegotiationConfig
import java.lang.RuntimeException

fun ContentNegotiationConfig.gson(
    config: (GsonBuilder.() -> Unit)? = null
) = if(!this.serializers.containsKey(Gson.contentType))
        this.serializers.put(ContentType.Application.JSON, Gson(config))
    else throw RuntimeException("Serializer Gson already configured!")