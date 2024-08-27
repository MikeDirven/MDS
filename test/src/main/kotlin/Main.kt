package nl.mdsystems

import nl.mdsystems.engine.EngineMain
import nl.mdsystems.engine.extensions.routing
import nl.mdsystems.engine.interfaces.*
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.routing.dsl.get
import nl.mdsystems.engine.routing.dsl.route
import java.io.File

fun main() = EngineMain {
    socket {
        host = "localhost"
        port = 8008
    }

    routing {
        rootPath = "henk"
    }

    modules(
        EngineMain::routes
    )
}

fun EngineMain.routes(){
    routing {
        route("test"){
            get {
                logger.info(this.request.path)
                respondText(
                    File("C:\\Users\\Mike\\Desktop\\MDS\\large-file.json")
                        .readText()
                )
            }
        }
    }
}