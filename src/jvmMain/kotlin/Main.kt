import mds.builders.get
import mds.builders.post
import mds.builders.routing
import mds.engine.classes.ContentType
import mds.engine.enums.HttpStatusCode
import mds.engine.interfaces.install
import mds.engine.logging.LoggingPlugin
import mds.plugins.serialization.ContentNegotiation
import mds.plugins.serialization.ContentNegotiation.Companion.install
import mds.plugins.serialization.extensions.receive
import mds.plugins.serialization.extensions.respond
import mds.plugins.serialization.gson.extensions.gson


fun main(args: Array<String>) = mds.engine.MdsEngine {
    port = 1500
    host = "0.0.0.0"
    entryPoint = mds.engine.MdsEngine::application
}.start()

data class Test(
    val test: String,
    val number: Int
)

fun mds.engine.MdsEngine.application() {
        install(LoggingPlugin)
        install(ContentNegotiation){
            gson {
                setPrettyPrinting()
            }
        }

        routing {
            post("esmee") { call ->
                val test = call.receive<Test>()
                println(test)
                call.respond(test)
            }

            get("esmee") { call ->
                call.respond(
                    HttpStatusCode.OK, ContentType.Application.JSON,
                    "{\"esmee\": {" +
                            "\"name\": \"esmee\"" +
                            "}" +
                            "}")
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