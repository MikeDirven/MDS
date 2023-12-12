package mds.engine.pipelines.subPipelines

import mds.engine.classes.HttpRequest
import mds.engine.classes.HttpResponse
import mds.engine.enums.Hook
import mds.engine.pipelines.ApplicationPipeline
import mds.engine.pipelines.Pipeline

class ResponsePipeline(
    override val applicationPipeline: ApplicationPipeline,
    val request: HttpRequest
) : Pipeline.SubPipeline {

    var response : HttpResponse = HttpResponse()

    internal fun handleResponseRequest(){
        try {
            applicationPipeline.requestHandler.handler(this)

            // Ready to send hook
            applicationPipeline.application.applicationHooks.filter { it.hook == Hook.RESPONSE_READY }.forEach {
                it.function(applicationPipeline.application, this, response)
            }

            //Trigger outgoing handler
            applicationPipeline.handleOutgoingPipeline()
        } catch (e: Throwable){
            val response = applicationPipeline.application.exceptionsToCatch.get(e::class)?.invoke(this, e)
                ?: applicationPipeline.application.defaultHandler.invoke(this, e)

            applicationPipeline.application.applicationHooks.filter { it.hook == Hook.RESPONSE_READY }.forEach {
                it.function(applicationPipeline.application, this, response)
            }
            applicationPipeline.socketWriter.write(response.toResponseString())
            applicationPipeline.socketWriter.flush()

            applicationPipeline.socketReader.close()
            applicationPipeline.socketWriter.close()
            applicationPipeline.socket.close()
        }
    }
}