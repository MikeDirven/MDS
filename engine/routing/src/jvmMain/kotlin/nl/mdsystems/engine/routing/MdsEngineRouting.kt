package nl.mdsystems.engine.routing

import nl.mdsystems.engine.core.classes.Component
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.MdsEngineLogging.Companion.getValue
import nl.mdsystems.engine.routing.interfaces.RouteBuilder
import nl.mdsystems.engine.routing.interfaces.RoutingBuilder
import nl.mdsystems.engine.socket.MdsEngineHttpSocket
import nl.mdsystems.engine.socket.MdsEngineHttpSocket.Companion.getValue
import java.util.concurrent.ConcurrentSkipListSet

class MdsEngineRouting(
    config: (RoutingBuilder.() -> Unit)? = null
)  {
    internal val logger by MdsEngineLogging
    internal val engineSocket by MdsEngineHttpSocket
    internal val activeRoutingContexts: ConcurrentSkipListSet<RouteBuilder>
        = ConcurrentSkipListSet()

    internal val builder: RoutingBuilder
        = object : RoutingBuilder(this, engineSocket::socket, logger) {
            override var rootPath: String = ""
                set(value) {
                    field = "/${value.trim('/')}"
                }
        }

    init {
        config?.let { builder.apply(it) }
    }

    fun getBuilder(): RoutingBuilder {
        return builder
    }

    companion object {
        val COMPONENT = Component<MdsEngineRouting>("MDS-Engine-Routing")
    }
}

