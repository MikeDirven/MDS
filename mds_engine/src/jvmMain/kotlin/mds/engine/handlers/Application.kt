package mds.engine.handlers

import mds.engine.classes.ApplicationHook
import mds.engine.classes.HttpRequest
import mds.engine.classes.HttpResponse
import mds.engine.enums.Hook
import mds.engine.enums.RequestMethods
import mds.engine.interfaces.*
import mds.engine.plugins.PluginKey
import mds.engine.logging.Tags
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import kotlin.reflect.KClass

open class Application() : Thread("Mds_Engine_Thread"), MdsEngineRequests, MdsEngineHooks, MdsEnginePlugins, MdsEngineExceptions {
    override val exceptionsToCatch: MutableMap<KClass<Exception>, ExceptionHandler> = mutableMapOf()
    override val applicationHooks: MutableList<ApplicationHook> = mutableListOf()
    override val applicationPlugins: MutableMap<PluginKey<*>, Any> = mutableMapOf()
    val routing: Routing = Routing(this)

    internal fun handleIncomingPipeline(clientSocket: Socket) {
        val writer = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))

        val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val request: HttpRequest = reader.readLine().split(" ").let { requestLine ->
            HttpRequest(
                this,
                method = RequestMethods.valueOf(requestLine[0]),
                path = requestLine[1].substringBefore("?"),
                queryParams = requestLine[1].substringAfterLast("?", "").readQueryParameters(),
                protocol = requestLine[2],
                outPutWriter = writer
            )
        }

        println("${Tags.methodColor(request.method)} ${request.method} - ${request.path} - ${request.queryParams}")

        // Request received hook
        applicationHooks.filter { it.hook == Hook.REQUEST_RECEIVED }.forEach {
            it.function(this, request, null)
        }

        try {
            // Receive Headers
            request.headers = readRequestHeaders(reader)

            if (request.method != RequestMethods.GET) {
                // Before body receive hook
                applicationHooks.filter { it.hook == Hook.BEFORE_BODY_READ }.forEach {
                    it.function(this, request, null)
                }

                // Receive body
                request.body = readRequestBody(reader, request.headers["Content-Length"]?.toInt() ?: 0)

                // After body received hook
                applicationHooks.filter { it.hook == Hook.BODY_RECEIVED }.forEach {
                    it.function(this, request, null)
                }
            }

            // Find Handler otherwise defaults to Not found
            val routesAvailable = routing.routes.filter { request.path.contains(it.path) }
            val handlersAvailable = mutableListOf<(request: HttpRequest) -> HttpResponse>().apply {
                routesAvailable.forEach { route ->
                    this.addAll(route.requestHandler.filter { it.path.trimStart('/') == request.path.trimStart('/') && it.method == request.method }
                        .map { it.handler })
                }
            }

            val response = handlersAvailable.firstOrNull()?.let { it(request) }

            // Ready to send hook
            applicationHooks.filter { it.hook == Hook.RESPONSE_READY }.forEach {
                it.function(this, request, response)
            }

            writer.write(
                sendResponseBody(
                    response?.toResponseString()
                        ?: "HTTP/1.1 404 Not Found\r\n\r\n"
                )
            )
            writer.flush()

            reader.close()
            writer.close()
            clientSocket.close()

            // Response send hook
            applicationHooks.filter { it.hook == Hook.RESPONSE_SEND }.forEach {
                it.function(this, request, response)
            }
        } catch (e: Exception){
            val response = exceptionsToCatch.get(e::class)?.invoke(request, e) ?: defaultHandler.invoke(request, e)

            applicationHooks.filter { it.hook == Hook.RESPONSE_READY }.forEach {
                it.function(this, request, response)
            }
            writer.write(response.toResponseString())
            writer.flush()

            reader.close()
            writer.close()
            clientSocket.close()
        }
    }
}