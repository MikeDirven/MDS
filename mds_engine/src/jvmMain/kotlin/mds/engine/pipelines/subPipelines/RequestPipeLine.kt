package mds.engine.pipelines.subPipelines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mds.engine.classes.HttpRequest
import mds.engine.enums.Hook
import mds.engine.interfaces.MdsEngineRequests
import mds.engine.pipelines.ApplicationPipeline
import mds.engine.pipelines.Pipeline

class RequestPipeLine(
    override val applicationPipeline: ApplicationPipeline,
    val request: HttpRequest
) : Pipeline.SubPipeline, MdsEngineRequests {

    internal suspend fun handleIncomingRequest(){
        try {
            // Receive Headers
            request.headers = readRequestHeaders(applicationPipeline.socketReader)
            applicationPipeline.receivePipeline = ReceivePipeline(applicationPipeline, request)
            applicationPipeline.receivePipeline.handleReceivePipeline()
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