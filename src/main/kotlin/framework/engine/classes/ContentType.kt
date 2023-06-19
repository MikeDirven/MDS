package framework.engine.classes

class ContentType(
    val actual: String
) {
    object Application {
        val json = ContentType("Application/JSON")
    }

    object Text {
        val plain = ContentType("text/plain")
    }
}