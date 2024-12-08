package nl.mdsystems.engine.templating.kotlinx.html

import kotlinx.html.HTML
import kotlinx.html.html
import kotlinx.html.stream.createHTML
import nl.mdsystems.engine.pipelines.ResponsePipeline
import nl.mdsystems.engine.pipelines.enums.HttpStatusCode

fun ResponsePipeline.respondHtml(statusCode: HttpStatusCode = HttpStatusCode.OK, html: HTML) {
    val byteArray = html.toString().toByteArray()
    connection.responseHeaders.add("Content-Type", "text/html")
    connection.sendResponseHeaders(statusCode.code, byteArray.size.toLong())
    connection.responseBody.write(byteArray)
}

fun ResponsePipeline.respondHtml(statusCode: HttpStatusCode = HttpStatusCode.OK, builder: HTML.() -> Unit) {
    val html = createHTML().html { builder() }.toString()
    val byteArray = html.toByteArray()
    connection.responseHeaders.add("Content-Type", "text/html")
    connection.sendResponseHeaders(statusCode.code, byteArray.size.toLong())
    connection.responseBody.write(byteArray)
}