package nl.mdsystems

import nl.mdsystems.engine.EngineMain
import nl.mdsystems.engine.extensions.routing
import nl.mdsystems.engine.interfaces.*
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.routing.dsl.get
import nl.mdsystems.engine.routing.dsl.route
import java.io.File

fun main(): Unit = EngineMain {
    socket {
        host = "0.0.0.0"
        port = 8008
    }

    routing {
        rootPath = "henk"
    }

    modules + EngineMain::routes
    modules add EngineMain::routes
    addModule(EngineMain::routes)
    modules(EngineMain::routes)
}

fun EngineMain.routes(){
    routing {
        route("test"){
            get {
                logger.info(this.request.path)
                respondText(
//                    File("C:\\Users\\Mike\\Desktop\\MDS\\large-file.json")
//                        .readText()
                    "Mike is goed bezig"
                )
            }
        }
    }
}