package nl.mdsystems.engine.pipelines.responders

import nl.mdsystems.engine.pipelines.ResponsePipeline
import nl.mdsystems.engine.pipelines.enums.HttpStatusCode
import java.io.File
import java.io.FileInputStream
import java.util.zip.GZIPOutputStream

fun ResponsePipeline.respondFile(file: File, statusCode: HttpStatusCode = HttpStatusCode.OK) {
    if (!file.exists() || !file.isFile) {
        throw IllegalArgumentException("The provided file does not exist or is not a file.")
    }

    val contentType = getContentType(file.extension)
    val fileBytes = file.readBytes()
    val acceptEncoding = request.headers.get("Accept-Encoding")?.firstOrNull()?.split(',')
    if (acceptEncoding?.contains("gzip") == true) {
        connection.responseHeaders.add("Content-Encoding", "gzip")
        connection.sendResponseHeaders(statusCode.code, fileBytes.size.toLong())
        val gzipOutputStream = GZIPOutputStream(connection.responseBody)
        gzipOutputStream.write(fileBytes)
    } else {
        connection.responseHeaders.add("Content-Type", contentType)
        connection.sendResponseHeaders(statusCode.code, fileBytes.size.toLong())
        connection.responseBody.write(fileBytes)
    }
}

private fun getContentType(extension: String): String {
    return when (extension.lowercase()) {
        "txt" -> "text/plain"
        "html", "htm" -> "text/html"
        "css" -> "text/css"
        "js" -> "application/javascript"
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "json" -> "application/json"
        "xml" -> "application/xml"
        "pdf" -> "application/pdf"
        "zip" -> "application/zip"
        "rar" -> "application/x-rar-compressed"
        "7z" -> "application/x-7z-compressed"
        "mp3" -> "audio/mpeg"
        "wav" -> "audio/wav"
        "ogg" -> "audio/ogg"
        "mp4" -> "video/mp4"
        "webm" -> "video/webm"
        "flv" -> "video/x-flv"
        "avi" -> "video/x-msvideo"
        "doc", "docx" -> "application/msword"
        "xls", "xlsx" -> "application/vnd.ms-excel"
        "ppt", "pptx" -> "application/vnd.ms-powerpoint"
        "rtf" -> "application/rtf"
        "csv" -> "text/csv"
        "svg" -> "image/svg+xml"
        "ttf" -> "font/ttf"
        "otf" -> "font/otf"
        "woff" -> "font/woff"
        "woff2" -> "font/woff2"
        else -> "application/octet-stream"
    }
}