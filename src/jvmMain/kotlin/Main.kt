import framework.builders.get
import framework.builders.post
import framework.builders.routing
import framework.engine.MdsEngine
import framework.engine.classes.ContentType
import framework.engine.enums.HttpStatusCode
import framework.engine.interfaces.install
import framework.engine.serialization.gson.GsonSerializer
import framework.engine.serialization.gson.receive
import framework.engine.serialization.gson.respond

fun main(args: Array<String>) = MdsEngine {
    port = 1500
    host = "0.0.0.0"
    entryPoint = ::application
}.start()

data class Test(
    val test: String,
    val number: Int
)

fun application(engine: MdsEngine) {
    engine.apply {
        install(GsonSerializer){
            contentType = ContentType.Application.json
        }

        routing {
            post("esmee") { call ->
                val test = call.receive<Test>()
                println(test.test)
                println(test.number)

                call.respond(
                    HttpStatusCode.OK,
                    ContentType.Application.json,
                    Test("mike", 29)
                )
            }

            get("esmee") { call ->
                call.respond(HttpStatusCode.OK, ContentType.Application.json,
                    "{\"esmee\": {" +
                            "\"name\": \"esmee\"" +
                            "}" +
                            "}")
            }
        }
    }
}
