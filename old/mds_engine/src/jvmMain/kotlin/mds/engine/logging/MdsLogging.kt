package mds.engine.logging

import mds.engine.enums.Hook
import mds.engine.handlers.Application
import mds.engine.logging.extensions.info
import mds.engine.pipelines.ApplicationPipeline
import mds.engine.pipelines.Pipeline
import mds.engine.pipelines.subPipelines.ReceivePipeline
import mds.engine.pipelines.subPipelines.RequestPipeLine
import mds.engine.pipelines.subPipelines.ResponsePipeline
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface MdsLogging {
    val logger : Logger
    fun Application.initLogging() {
        on(Hook.BODY_RECEIVED){ call, _  ->
            call?.let {
                when(it){
                    is ApplicationPipeline -> info("${Tags.engine} Request body: null")
                    is RequestPipeLine -> info("${Tags.methodColor(it.request.method)} Request body: \n${it.request.body}")
                    is ReceivePipeline -> info("${Tags.methodColor(it.request.method)} Request body: \n${it.request.body}")
                    is ResponsePipeline -> info("${Tags.methodColor(it.request.method)} Request body: \n${it.request.body}")
                    is Pipeline.SubPipeline -> info("${Tags.engine} Request body: null")
                }
            }
        }
    }
}