package nl.mdsystems

import kotlinx.html.DIV
import kotlinx.html.body
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.h1
import kotlinx.html.html
import kotlinx.html.p
import kotlinx.html.stream.createHTML
import nl.mdsystems.engine.EngineMain
import nl.mdsystems.engine.extensions.routing
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.mdsModule
import nl.mdsystems.engine.pipelines.responders.respondFile
import nl.mdsystems.engine.routing.dsl.get
import nl.mdsystems.engine.routing.dsl.route
import nl.mdsystems.engine.templating.kotlinx.html.respondHtml
import java.io.File

fun main(): Unit = EngineMain {
    socket {
        host = "0.0.0.0"
        port = 8008
    }

    routing {
        rootPath = "henk"
    }

    modules {
        waitForModules = true
        + routing()
    }
}

fun routing() = mdsModule("Test_Routing"){
    routing {
        route("test"){
            get("herman") {
                logger.info(this.request.path)
                respondFile(
                    File("C:\\Users\\Mike\\Desktop\\MDS\\index.html")
                )
//                respondHtml {
//                    body {
//                        h1 {
//                            +"This is a test"
//                        }
//                        p {
//                            +"The current path is: ${request.path}"
//                        }
//                        p {
//                            +"Esmee dit is niet vanzelf sprekend :)"
//                        }
//                    }
//                }
            }
        }
    }
}