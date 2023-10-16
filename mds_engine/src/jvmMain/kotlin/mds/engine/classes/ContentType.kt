package mds.engine.classes

class ContentType(
    val actual: String
) {
    object Application {
        val JSON = ContentType("Application/JSON")
    }

    object Text {
        val plain = ContentType("text/plain")
    }

    companion object {
        fun parse(contentType: String) : ContentType =
            contentType.split('/').let { split ->
                when{
                    split[0].contains('/') -> throw ClassCastException("Unable to cast string to ContentType")
                    split[1].contains('/') -> throw ClassCastException("Unable to cast string to ContentType")
                    split[0].contains(' ') -> throw ClassCastException("Unable to cast string to ContentType")
                    split[1].contains(' ') -> throw ClassCastException("Unable to cast string to ContentType")
                    else -> ContentType("${split[0]}/${split[1]}")
                }
            }
    }
}