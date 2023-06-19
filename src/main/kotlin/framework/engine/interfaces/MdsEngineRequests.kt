package framework.engine.interfaces

import java.io.BufferedReader

interface MdsEngineRequests {
    fun readRequestBody(reader: BufferedReader): String {
        val contentLengthHeader = "Content-Length: "
        var contentLength = 0
        var line: String?

        while (true) {
            line = reader.readLine()
            if (line == null || line.isEmpty())
                break

            if (line.startsWith(contentLengthHeader)) {
                contentLength = line.substring(contentLengthHeader.length).toInt()
            }
        }

        val requestBody = CharArray(contentLength)
        reader.read(requestBody, 0, contentLength)

        return String(requestBody)
    }

    fun String.readQueryParameters(): Map<String, String> =  mutableMapOf<String, String>().also { map ->
        if(this.isNotEmpty()) this.split("&").forEach { queryEntry ->
            queryEntry.split("=").let { parsedEntry ->
                map.put(parsedEntry[0], parsedEntry[1])
            }
        }
    }

    fun sendResponseBody(responseBody: String): String {
        // Handle GET request
        return responseBody
    }
}