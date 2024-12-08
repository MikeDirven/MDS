package nl.mdsystems.engine.pipelines.responders

import nl.mdsystems.engine.pipelines.ResponsePipeline
import nl.mdsystems.engine.pipelines.enums.HttpStatusCode

fun ResponsePipeline.respondHtml(html: String, statusCode: HttpStatusCode = HttpStatusCode.OK) {
    val byteArray = html.toByteArray()
    connection.responseHeaders.add("Content-Type", "text/html")
    connection.sendResponseHeaders(statusCode.code, byteArray.size.toLong())
    connection.responseBody.write(byteArray)
}