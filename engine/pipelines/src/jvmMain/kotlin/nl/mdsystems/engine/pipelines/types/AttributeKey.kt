package nl.mdsystems.engine.pipelines.types

class AttributeKey<T>(val name: String)

fun <T> String.asAttributeKey(): AttributeKey<T>
    = AttributeKey(this)

inline fun <reified T> T.asAttributeKey(): AttributeKey<T>
    = AttributeKey(T::class.java.simpleName)