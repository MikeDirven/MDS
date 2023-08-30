import framework.builders.get
import framework.builders.post
import framework.builders.routing
import framework.engine.MdsEngine
import framework.engine.classes.ContentType
import framework.engine.enums.HttpStatusCode
import framework.engine.interfaces.install
import framework.engine.logging.LoggingPlugin
import framework.engine.serialization.ContentNegotiation
import framework.engine.serialization.extensions.receive
import framework.engine.serialization.extensions.respond
import framework.engine.serialization.gson.extensions.gson

fun main(args: Array<String>) = MdsEngine {
    port = 1500
    host = "0.0.0.0"
    entryPoint = MdsEngine::application
}.start()

data class Test(
    val test: String,
    val number: Int
)

fun MdsEngine.application() {
        install(LoggingPlugin)
        install(ContentNegotiation){
            gson {
                this.setPrettyPrinting()
            }
        }

        routing {
            post("esmee") { call ->
                val test = call.receive<Test>()
                println(test)

                call.respond(test)
            }

            get("esmee") { call ->
                call.respond(HttpStatusCode.OK, ContentType.Application.JSON,
                    "{\"esmee\": {" +
                            "\"name\": \"esmee\"" +
                            "}" +
                            "}")
            }
        }
    }
}


val gsonString = """
    {
      "active" : 1716359544,
      "activity" : "#bookworm",
      "activitySourceDocuments" : "man.cgi",
      "activityStatus" : "invalid",
      "activityStep" : "0x6441d37b4b96471da699d389b00eba9535bd14c7a47a40398dc91f257ba41462",
      "activityStepStatus" : "failed",
      "activityType" : "rejected",
      "activityTypeDescription" : "Beyond our ken",
      "activityUsers" : "Lawrence",
      "articles" : 9700,
      "assigned" : "Bugger@aol.bz",
      "current" : "+664047621036",
      "end" : "car.z",
      "externalReference" : "jpeg",
      "linkedActivity" : "https://MindlessBobcat.ru/education/moment/job/hour",
      "location" : "Denmark, Pristina, Upper East Biacunk, Grove Lane 16",
      "locationsAmount" : 16,
      "orders" : 5411,
      "recordId" : 1995923155,
      "requestedTime" : "11:19:10.000",
      "selectionTime" : "01:57:10.000",
      "started" : "61",
      "status" : 672178617,
      "statusDescription" : "Beat around the bush",
      "type" : "failed",
      "typeDescription" : "A rolling stone gathers no moss",
      "workplace" : "Malawi, Kingdom, Drussatnend Circle, Willow Court 2"
    }
""".trimIndent()