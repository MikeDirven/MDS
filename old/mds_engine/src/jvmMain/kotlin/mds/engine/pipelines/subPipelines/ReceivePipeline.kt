package mds.engine.pipelines.subPipelines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mds.engine.classes.HttpRequest
import mds.engine.enums.Hook
import mds.engine.enums.RequestMethods
import mds.engine.interfaces.MdsEngineRequests
import mds.engine.pipelines.ApplicationPipeline
import mds.engine.pipelines.Pipeline

class ReceivePipeline(
    override val applicationPipeline: ApplicationPipeline,
    val request: HttpRequest
) : Pipeline.SubPipeline, MdsEngineRequests {
    internal suspend fun handleReceivePipeline(){
        try {
            if (request.method != RequestMethods.GET) {
                // Before body receive hook
                applicationPipeline.application.applicationHooks.filter { it.hook == Hook.BEFORE_BODY_READ }.forEach {
                    it.function(applicationPipeline.application, this, null)
                }

                // Receive body
                request.body = readRequestBody(applicationPipeline.socketReader, request.headers["Content-Length"]?.toInt() ?: 0)

                // After body received hook
                applicationPipeline.application.applicationHooks.filter { it.hook == Hook.BODY_RECEIVED }.forEach {
                    it.function(applicationPipeline.application, this, null)
                }
            }

            applicationPipeline.responsePipeline = ResponsePipeline(applicationPipeline, request)
            applicationPipeline.responsePipeline.handleResponseRequest()
        } catch (e: Throwable){
            val response = applicationPipeline.application.exceptionsToCatch.get(e::class)?.invoke(this, e)
                ?: applicationPipeline.application.defaultHandler.invoke(this, e)

            applicationPipeline.application.applicationHooks.filter { it.hook == Hook.RESPONSE_READY }.forEach {
                it.function(applicationPipeline.application, this, response)
            }
            withContext(Dispatchers.IO) {
                applicationPipeline.socketWriter.write(response.toResponseString())
                applicationPipeline.socketWriter.flush()

                applicationPipeline.socketReader.close()
                applicationPipeline.socketWriter.close()
                applicationPipeline.socket.close()
            }
        }
    }
}