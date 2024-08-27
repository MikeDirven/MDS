package nl.mdsystems.engine.routing

import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.routing.interfaces.EngineRoutingConfig
import nl.mdsystems.engine.routing.interfaces.RoutingBuilder
import kotlin.reflect.KProperty0

class MdsEngineRouting(
    internal val socketContext: KProperty0<HttpServer>,
    internal val loggingContext: KProperty0<MdsEngineLogging>,
    config: (EngineRoutingConfig.() -> Unit)? = null
)  {
    internal val builder: RoutingBuilder
        = object : RoutingBuilder(socketContext, loggingContext) {
            override var rootPath: String = ""
                set(value) {
                    field = "/${value.trim('/')}"
                }
        }

    init {
        config?.invoke(builder)
    }

    fun getBuilder(): RoutingBuilder {
        return builder
    }
}

