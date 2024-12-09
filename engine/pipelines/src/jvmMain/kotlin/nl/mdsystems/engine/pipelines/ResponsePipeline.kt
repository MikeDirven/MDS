package nl.mdsystems.engine.pipelines

import com.sun.net.httpserver.Headers
import com.sun.net.httpserver.HttpExchange
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.pipelines.enums.HttpMethod
import nl.mdsystems.engine.pipelines.enums.HttpStatusCode
import nl.mdsystems.engine.pipelines.enums.UriConstants
import nl.mdsystems.engine.pipelines.interfaces.PipelineRoutesBuilder
import nl.mdsystems.engine.pipelines.objects.RequestObject
import nl.mdsystems.engine.pipelines.objects.ResponseObject
import nl.mdsystems.engine.pipelines.types.AttributeKey
import kotlin.reflect.KProperty0

class ResponsePipeline(
    val connection: HttpExchange
) : PipelineRoutesBuilder {
    val logger by MdsEngineLogging
    val attributes: MutableMap<AttributeKey<*>, Any> = mutableMapOf()

    val request = object : RequestObject {
        override val method: HttpMethod = HttpMethod.valueOf(connection.requestMethod)
        override val host: String = connection.httpContext.server.address.hostString
        override val port: Int = connection.httpContext.server.address.port
        override val path: String = connection.requestURI.path.removePrefix("/")
        override val headers: Headers = connection.requestHeaders
        override val queryParameters: Map<String, String> = readQueryParameters()
        override val pathParameters: Map<String, String> = readPathParameters()
        override val body: ByteArray = connection.requestBody.readAllBytes()
    }
    val response = object : ResponseObject {
        override val headers = connection.responseHeaders
        override var body: ByteArray = ByteArray(0)
            get() {
                throw UnsupportedOperationException("Reading response body is not supported")
            }
            set(value) {
                field = value.also {
                    connection.responseBody.write(it)
                }
            }
    }

    /**
     * Parses the query parameters from the request URI and returns them as a map.
     *
     * @return A map containing the query parameters, where the keys are the parameter names and the values are the parameter values.
     *
     * @throws UnsupportedOperationException If the request URI does not contain any query parameters.
     *
     * @see HttpExchange.getRequestURI
     * @see String.split
     * @see Map.buildMap
     */
    internal fun readQueryParameters(): Map<String, String> {
        return connection.requestURI.query?.split("&").let {
            buildMap {
                it?.forEach { entry ->
                    val (key, value) = entry.split("=")
                    put(key, value)
                }
            }
        }
    }

    /**
     * Parses the path parameters from the request URI and returns them as a map.
     *
     * @return A map containing the path parameters, where the keys are the parameter names and the values are the parameter values.
     *
     * @throws UnsupportedOperationException If the request URI does not contain any path parameters or if the path parameter keys are not found in the HTTP context attributes.
     *
     * @see HttpExchange
     * @see UriConstants.PATH_PARAMETERS
     * @see HttpContext.attributes
     * @see String.split
     * @see Map.buildMap
     */
    internal fun readPathParameters(): Map<String, String> {
        val pathParameterKeys: Map<String, Int>? = connection.httpContext.attributes.get(
            UriConstants.PATH_PARAMETERS.name
        ) as? Map<String, Int>

        val pathSegments = connection.requestURI.path.trim('/').split("/")

        return buildMap {
            pathParameterKeys?.forEach { (key, index) ->
                if (index < pathSegments.size) {
                    put(key, pathSegments[index])
                } else {
                    throw UnsupportedOperationException("Not enough path segments to match path parameter '$key'")
                }
            }
        }
    }
}