package mds.engine.pipelines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mds.engine.classes.HttpRequest
import mds.engine.classes.RequestHandler
import mds.engine.enums.Hook
import mds.engine.enums.RequestMethods
import mds.engine.handlers.Application
import mds.engine.interfaces.MdsEngineExceptions
import mds.engine.interfaces.MdsEngineRequests
import mds.engine.logging.Tags
import mds.engine.logging.extensions.info
import mds.engine.pipelines.subPipelines.ReceivePipeline
import mds.engine.pipelines.subPipelines.RequestPipeLine
import mds.engine.pipelines.subPipelines.ResponsePipeline
import mds.exceptions.NotFoundException
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.net.SocketOptions
import kotlin.coroutines.CoroutineContext

class ApplicationPipeline(
    private val coroutineContext: CoroutineContext,
    val application: Application,
    internal val socket: Socket,
    val resolve: ApplicationPipeline.() -> Unit
) : Pipeline, MdsEngineRequests {
    internal lateinit var socketWriter: BufferedWriter
    internal lateinit var socketReader: BufferedReader

    internal lateinit var requestHandler: RequestHandler

    // Sub pipelines
    lateinit var requestPipeline: RequestPipeLine
    lateinit var receivePipeline: ReceivePipeline
    lateinit var responsePipeline: ResponsePipeline

    init {
        CoroutineScope(coroutineContext).launch {
            try {
                handleIncomingPipeline()
            } catch (e: Throwable) {
                val response =
                    application.exceptionsToCatch.get(e::class)?.invoke(this@ApplicationPipeline, e) ?: application.defaultHandler.invoke(
                        this@ApplicationPipeline,
                        e
                    )

                application.applicationHooks.filter { it.hook == Hook.RESPONSE_READY }.forEach {
                    it.function(application, this@ApplicationPipeline, response)
                }
                socketWriter.write(response.toResponseString())
                socketWriter.flush()

                socketReader.close()
                socketWriter.close()
                socket.close()
            }
        }
    }

    private suspend fun handleIncomingPipeline(){
        val requestReader: HttpRequest
        withContext(Dispatchers.IO) {
            socketWriter = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
            socketReader = BufferedReader(InputStreamReader(socket.getInputStream()))
            requestReader = socketReader.readLine().split(" ").let { requestLine ->
                HttpRequest(
                    method = RequestMethods.valueOf(requestLine[0]),
                    path = requestLine[1].substringBefore("?"),
                    queryParams = requestLine[1].substringAfterLast("?", "").readQueryParameters(),
                    protocol = requestLine[2],
                    outPutWriter = socketWriter
                )
            }
        }


        application.info("${Tags.methodColor(requestReader.method)} ${requestReader.path} - ${requestReader.queryParams}")

        // Request received hook
        application.applicationHooks.filter { it.hook == Hook.REQUEST_RECEIVED }.forEach {
            it.function(this.application, requestPipeline, null)
        }

        // Find Handler otherwise defaults to Not found
        application.info("${Tags.methodColor(requestReader.method)} Route matcher")
        val routesAvailable = application.routing.routes.filter { requestReader.path.contains(it.path) }.mapNotNull { route ->
            if(requestReader.path.trimStart('/').contains(route.path.trimStart('/'), true)){
                application.info("${Tags.methodColor(requestReader.method)} Matches route: ${route.path}")
                route.requestHandler
            } else null
        }

        requestHandler = routesAvailable.firstNotNullOfOrNull { it.firstOrNull { it.method == requestReader.method } } ?: throw NotFoundException()
        application.info("${Tags.methodColor(requestReader.method)} Matched route handler ${requestHandler.method}:${requestHandler.path}")

        // Construct request pipeline
        requestPipeline = RequestPipeLine(
            this,
            requestReader
        )
        requestPipeline.handleIncomingRequest()
    }

    internal suspend fun handleOutgoingPipeline(){
        withContext(Dispatchers.IO) {
            socketWriter.write(
                sendResponseBody(
                    responsePipeline.response.toResponseString()
                        ?: "HTTP/1.1 404 Not Found\r\n\r\n"
                )
            )
            socketWriter.flush()

            socketReader.close()
            socketWriter.close()
            socket.close()
        }


        // Response send hook
        application.applicationHooks.filter { it.hook == Hook.RESPONSE_SEND }.forEach {
            it.function(application, this, responsePipeline.response)
        }
    }
}