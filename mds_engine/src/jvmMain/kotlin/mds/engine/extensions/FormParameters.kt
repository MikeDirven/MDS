package mds.engine.extensions

import mds.engine.pipelines.subPipelines.ResponsePipeline

fun ResponsePipeline.receiveFormParameters() : List<Pair<String, String>>{
    return request.body.toString().split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().map {
        it.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().let {
            Pair(it[0], it[1])
        }
    }
}