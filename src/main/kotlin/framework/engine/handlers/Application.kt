package framework.engine.handlers

import framework.engine.classes.ApplicationHook
import framework.engine.classes.HttpRequest
import framework.engine.classes.HttpResponse
import framework.engine.enums.Hook
import framework.engine.enums.RequestMethods
import framework.engine.interfaces.MdsEngineHooks
import framework.engine.interfaces.MdsEngineRequests
import framework.engine.logging.Tags
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

open class Application() : Thread("Mds_Engine_Thread"),  MdsEngineRequests, MdsEngineHooks {
    override val applicationHooks: MutableList<ApplicationHook> = mutableListOf()
    val routing: Routing = Routing(this)

    internal fun handleIncomingPipeline(clientSocket: Socket) {
        val writer = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))

        val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val request: HttpRequest = reader.readLine().split(" ").let { requestLine ->
            HttpRequest(
                RequestMethods.valueOf(requestLine[0]),
                requestLine[1].substringBefore("?"),
                requestLine[1].substringAfterLast("?", "").readQueryParameters(),
                requestLine[2])
        }

        println("${Tags.methodColor(request.method)} ${request.method} - ${request.path} - ${request.queryParams}")

        // Request received hook
        applicationHooks.filter { it.hook == Hook.REQUEST_RECEIVED }.forEach {
            it.function(this, request)
        }

        if(request.method != RequestMethods.GET){
            // Before body receive hook
            applicationHooks.filter { it.hook == Hook.BEFORE_BODY_READ }.forEach {
                it.function(this, request)
            }

            // Receive body
            request.body = readRequestBody(reader)

            // After body received hook
            applicationHooks.filter { it.hook == Hook.BODY_RECEIVED }.forEach {
                it.function(this, request)
            }
        }

        // Find Handler otherwise defaults to Not found
        val routesAvailable = routing.routes.filter { request.path.contains(it.path)}
        val handlersAvailable = mutableListOf<(request: HttpRequest) -> HttpResponse>().apply{
            routesAvailable.forEach { route ->
                this.addAll(route.requestHandler.filter { it.path.trimStart('/') == request.path.trimStart('/') && it.method == request.method }.map { it.handler })
            }
        }

        val response =  handlersAvailable.firstOrNull()?.let { it(request) }?.toResponseString()
            ?: "HTTP/1.1 404 Not Found\r\n\r\n"

        // Ready to send hook
        applicationHooks.filter { it.hook == Hook.RESPONSE_READY }.forEach {
            it.function(this, request)
        }

        writer.write(sendResponseBody(response))
        writer.flush()

        reader.close()
        writer.close()
        clientSocket.close()

        // Response send hook
        applicationHooks.filter { it.hook == Hook.RESPONSE_SEND }.forEach {
            it.function(this, request)
        }
    }
}