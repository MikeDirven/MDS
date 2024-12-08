package nl.mdsystems.engine.socket

import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import nl.mdsystems.engine.EngineMain
import nl.mdsystems.engine.extensions.getElapsedTimeInSecondsWithDecimal
import nl.mdsystems.engine.logging.functions.error
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.socket.interfaces.EngineSocketConfig
import java.net.InetAddress
import java.net.InetSocketAddress
import kotlin.coroutines.EmptyCoroutineContext

class EngineHttpSocket(config: (EngineSocketConfig.() -> Unit)? = null) {
    internal val configuration: EngineSocketConfig = object : EngineSocketConfig {
        override var host: String = "0.0.0.0"
        override var port: Int = 80
        override var backlog: Int = Int.MAX_VALUE
    }

    init {
        config?.invoke(configuration)
    }

    internal object EnvironmentConfig {
        val PORT: Int? = System.getenv("PORT")?.toIntOrNull()
        val HOST: String? = System.getenv("HOST")?.toString()
    }

    // Create context
    internal val socket: HttpServer by lazy {
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

    fun EngineMain.createSocketContext() {

    }

    /**
     * Sets the executor for handling incoming requests. This executor is responsible for dispatching
     * the request handling tasks to a coroutine dispatcher.
     *
     * @param dispatcherChooser A function that returns an [ExecutorCoroutineDispatcher] to be used for
     * dispatching the request handling tasks. This function is called each time a new request arrives.
     *
     * The executor is set to the [HttpServer] instance, which will create a new coroutine for each
     * incoming request and execute it using the chosen dispatcher.
     *
     * The executor also increments the total request currently processing metric and logs any exceptions
     * that occur during the request handling.
     *
     * @see ExecutorCoroutineDispatcher
     * @see HttpServer
     * @see metrics.incrementTotalRequestCurrentlyProcessing
     * @see logging.error
     */
    fun EngineMain.setRequestExecutor(dispatcherChooser: () -> ExecutorCoroutineDispatcher) {
        socket.setExecutor {
            metrics.incrementTotalRequestCurrentlyProcessing()

            runCatching {
                dispatcherChooser().dispatch(EmptyCoroutineContext, it)
            }.onFailure { exception ->
                logging.error(exception)
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
     * @see getElapsedTimeInSecondsWithDecimal
     * @see isInterrupted
     * @see socket.stop
     */
    fun EngineMain.startServer() {
        socket.start()

        logging.info("Listening on: ${socket.address}")
        logging.info("Application started in: ${getElapsedTimeInSecondsWithDecimal()} seconds")

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
    fun EngineMain.stopServer() {
        logging.info("Stopping Http socket...")
        socket.stop(0)
        logging.info("Http socket stopped.")
    }
}