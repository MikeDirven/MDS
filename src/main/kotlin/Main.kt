
import framework.builders.get
import framework.builders.post
import framework.builders.routing
import framework.engine.MdsEngine
import framework.engine.classes.ContentType
import framework.engine.enums.HttpStatusCode

fun main(args: Array<String>) = MdsEngine {
    port = 1500
    host = "0.0.0.0"
    entryPoint = ::application
}.start()


fun application(engine: MdsEngine) = engine.routing {
    post("esmee") { call ->
        call.respond(
            HttpStatusCode.OK, ContentType.Application.json,
            "{\"esmee\": {" +
                "\"name\": \"esmee\"" +
                "}" +
                "}")
    }

    get("esmee") { call ->
        call.respond(HttpStatusCode.OK, ContentType.Application.json,
            "{\"esmee\": {" +
                "\"name\": \"esmee\"" +
                "}" +
                "}")
    }
}