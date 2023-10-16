package mds.engine.interfaces

import java.io.BufferedReader

interface MdsEngineRequests {
    fun readRequestBody(reader: BufferedReader, contentLength: Int): String {
        val requestBody = CharArray(contentLength)
        reader.read(requestBody, 0, contentLength)

        return String(requestBody)
    }

    fun readRequestHeaders(reader: BufferedReader): Map<String, String> {
        val requestHeaders: MutableMap<String, String> = mutableMapOf()
        var line: String?

        while (true){
            line = reader.readLine()
            if (line.isNullOrEmpty())
                break

            val headerParts = line.split(":", limit = 2)
            if (headerParts.size == 2) {
                val headerName = headerParts[0].trim()
                val headerValue = headerParts[1].trim()
                requestHeaders[headerName] = headerValue
            }
        }

        return requestHeaders
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