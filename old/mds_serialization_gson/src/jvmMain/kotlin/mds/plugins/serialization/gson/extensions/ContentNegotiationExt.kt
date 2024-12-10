package mds.plugins.serialization.gson.extensions

import com.google.gson.GsonBuilder
import mds.engine.classes.ContentType
import mds.plugins.serialization.gson.Gson
import mds.plugins.serialization.interfaces.ContentNegotiationConfig
import java.lang.RuntimeException

fun ContentNegotiationConfig.gson(
    config: (GsonBuilder.() -> Unit)? = null
) = if(!this.serializers.containsKey(Gson.contentType))
        this.serializers.put(ContentType.Application.JSON, Gson(config))
    else throw RuntimeException("Serializer Gson already configured!")