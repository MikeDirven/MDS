package mds.engine.classes

import mds.engine.enums.RequestMethods
import mds.engine.pipelines.subPipelines.ResponsePipeline

class RequestHandler(
    val path: String,
    val method: RequestMethods,
    val handler: suspend (call: ResponsePipeline) -> HttpResponse
)