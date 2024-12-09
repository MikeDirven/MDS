package nl.mdsystems.engine.routing

import com.sun.net.httpserver.HttpServer
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.routing.interfaces.EngineRoutingConfig
import nl.mdsystems.engine.routing.interfaces.RouteBuilder
import nl.mdsystems.engine.routing.interfaces.RoutingBuilder
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.reflect.KProperty0

class MdsEngineRouting(
    internal val socketContext: KProperty0<HttpServer>,
    config: (EngineRoutingConfig.() -> Unit)? = null
)  {
    internal val logger by MdsEngineLogging
    internal val activeRoutingContexts: ConcurrentSkipListSet<RouteBuilder>
        = ConcurrentSkipListSet()

    internal val builder: RoutingBuilder
        = object : RoutingBuilder(this, socketContext, logger) {
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

