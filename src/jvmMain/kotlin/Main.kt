import mds.builders.get
import mds.builders.post
import mds.builders.routing
import mds.engine.MdsEngine
import mds.engine.classes.ContentType
import mds.engine.enums.HttpStatusCode
import mds.engine.interfaces.install
import mds.engine.logging.LoggingPlugin
import mds.plugins.serialization.ContentNegotiation
import mds.plugins.serialization.ContentNegotiation.Companion.install
import mds.plugins.serialization.extensions.receive
import mds.plugins.serialization.extensions.respond
import mds.plugins.serialization.gson.extensions.gson
import mds.plugins.sessions.Sessions
import mds.plugins.sessions.dsl.getSession
import mds.plugins.sessions.dsl.setSession
import mds.plugins.sessions.interfaces.MemorySessionsProvider


fun main(args: Array<String>) = mds.engine.MdsEngine {
    port = 1500
    host = "0.0.0.0"
    entryPoint = mds.engine.MdsEngine::application
}.start()

data class Test(
    val test: String,
    val number: Int
)

fun MdsEngine.application() {
    install(LoggingPlugin)
    install(ContentNegotiation){
        gson {
            setPrettyPrinting()
        }
    }
    install(Sessions){
        sessionProvider = MemorySessionsProvider("test"){

        }
    }

    routing {
        post("esmee") { call ->
            call.setSession("hello")
            val test = call.receive<Test>()
            call.respond(test)
        }

        get("esmee") { call ->
            val session = call.getSession<String>()
            println("session")
            call.respond(
                HttpStatusCode.OK, ContentType.Application.JSON, Test("mike", 1500))
        }
    }
}