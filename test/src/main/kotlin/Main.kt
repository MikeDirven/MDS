package nl.mdsystems

import nl.mdsystems.engine.MdsEngineMain
import nl.mdsystems.engine.logging.extensions.logging
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.modules.classes.mdsModule
import nl.mdsystems.engine.modules.extensions.modules
import nl.mdsystems.engine.pipelines.responders.respondFile
import nl.mdsystems.engine.routing.dsl.get
import nl.mdsystems.engine.routing.dsl.route
import nl.mdsystems.engine.routing.extensions.routing
import nl.mdsystems.engine.socket.extensions.socket
import nl.mdsystems.engine.threading.extensions.threading
import java.io.File

fun main(): Unit = MdsEngineMain {
    threading {

    }
    socket {
        host = "0.0.0.0"
        port = 8008
    }

    logging {

    }

    routing {
        rootPath = "henk"
    }

    modules {
        waitForModules = true
        + routingModule()
    }
}.start()

fun routingModule() = mdsModule("Test_Routing"){
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