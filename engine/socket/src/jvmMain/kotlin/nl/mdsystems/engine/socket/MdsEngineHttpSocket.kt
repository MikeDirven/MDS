package nl.mdsystems.engine.socket

import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import nl.mdsystems.engine.core.classes.Component
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.MdsEngineLogging.Companion.getValue
import nl.mdsystems.engine.logging.functions.error
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.metrics.MdsEngineMetrics
import nl.mdsystems.engine.metrics.MdsEngineMetrics.Companion.getValue
import nl.mdsystems.engine.socket.interfaces.EngineSocketConfig
import nl.mdsystems.utils.getElapsedTimeInSecondsWithDecimal
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KProperty

class MdsEngineHttpSocket(config: (EngineSocketConfig.() -> Unit)? = null) {
    internal val metrics by MdsEngineMetrics
    internal val logger by MdsEngineLogging
    internal val configuration: EngineSocketConfig = object : EngineSocketConfig {
        override var host: String = "0.0.0.0"
        override var port: Int = 80
        override var backlog: Int = Int.MAX_VALUE
    }

    init {
        config?.invoke(configuration)

        instance.set(this)
    }

    internal object EnvironmentConfig {
        val PORT: Int? = System.getenv("PORT")?.toIntOrNull()
        val HOST: String? = System.getenv("HOST")?.toString()
    }

    // Create context
    val socket: HttpServer by lazy {
        HttpServer.create(
            InetSocketAddress(
                InetAddress.getByName(
                    EnvironmentConfig.HOST ?: configuration.host
                ),
                EnvironmentConfig.PORT ?: configuration.port,
            ),
            configuration.backlog
        )
    }

    /**
     * Sets the executor for handling incoming requests. This executor is responsible for dispatching
     * the request handling tasks to a coroutine dispatcher.
     *
     * @param dispatcherChooser A function that returns an [kotlinx.coroutines.ExecutorCoroutineDispatcher] to be used for
     * dispatching the request handling tasks. This function is called each time a new request arrives.
     *
     * The executor is set to the [HttpServer] instance, which will create a new coroutine for each
     * incoming request and execute it using the chosen dispatcher.
     *
     * The executor also increments the total request currently processing metric and logs any exceptions
     * that occur during the request handling.
     *
     * @see kotlinx.coroutines.ExecutorCoroutineDispatcher
     * @see HttpServer
     * @see metrics.incrementTotalRequestCurrentlyProcessing
     * @see logging.error
     */
    fun setRequestExecutor(dispatcherChooser: () -> ExecutorCoroutineDispatcher) {
        socket.setExecutor {
            metrics?.incrementTotalRequestCurrentlyProcessing()

            runCatching {
                dispatcherChooser().dispatch(EmptyCoroutineContext, it)
            }.onFailure { exception ->
                logger.error(exception)
            }
        }
    }

    /**
     * Starts the HTTP server and begins listening for incoming requests.
     *
     * This function initializes the HTTP server by calling the [HttpServer.start] method. It then logs
     * the server's address and the time taken to start the application. After starting the server, the
     * function enters a loop that continues until the application is interrupted. Inside the loop, the
     * function checks for interruption and stops the server when the application is interrupted.
     *
     * @return Unit
     *
     * @see HttpServer.start
     * @see logging.info
     * @see nl.mdsystems.engine.extensions.getElapsedTimeInSecondsWithDecimal
     * @see isInterrupted
     * @see socket.stop
     */
    fun startServer() {
        socket.start()

        logger.info("Listening on: ${socket.address}")
        logger.info("Application started in: ${getElapsedTimeInSecondsWithDecimal()} seconds")

        // Keep thread running until interrupted
        while (!Thread.currentThread().isInterrupted) {
            // Keep thread alive without busy-waiting
            Thread.sleep(100)
        }

        socket.stop(0)
    }

    /**
     * Stops the HTTP server and releases any resources associated with it.
     *
     * This function logs a message indicating that the HTTP server is being stopped, then calls the
     * [HttpServer.stop] method with a timeout of 0 to immediately stop the server. After stopping the
     * server, it logs a message indicating that the HTTP socket has been stopped.
     *
     * @return Unit
     *
     * @see HttpServer.stop
     * @see logging.info
     */
    fun stopServer() {
        logger.info("Stopping Http socket...")
        socket.stop(0)
        logger.info("Http socket stopped.")
    }

    companion object {
        val COMPONENT = Component<MdsEngineHttpSocket>("MDS-Engine-Http-Socket")

        val instance: AtomicReference<MdsEngineHttpSocket?> = AtomicReference<MdsEngineHttpSocket?>(null)

        fun get() = instance.get()

        operator fun getValue(thisRef: Any?, property: KProperty<*>) : MdsEngineHttpSocket {
            return instance.get() ?: MdsEngineHttpSocket().also { instance.set(it) }
        }
    }
}