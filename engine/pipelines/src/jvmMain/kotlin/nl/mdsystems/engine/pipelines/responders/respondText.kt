package nl.mdsystems.engine.pipelines.responders

import nl.mdsystems.engine.pipelines.ResponsePipeline
import nl.mdsystems.engine.pipelines.enums.HttpStatusCode

fun ResponsePipeline.respondText(text: String, statusCode: HttpStatusCode = HttpStatusCode.OK){
    val byteArray = text.toByteArray()
    connection.responseHeaders.add("Content-Type", "text/plain")
    connection.sendResponseHeaders(statusCode.code, byteArray.size.toLong())
    connection.responseBody.write(byteArray)
}