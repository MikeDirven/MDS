package nl.mdsystems.engine.pipelines.objects

import com.sun.net.httpserver.Headers
import nl.mdsystems.engine.pipelines.enums.HttpMethod

/**
 * Represents an HTTP request object.
 *
 * This interface provides access to various components of an HTTP request, such as the method, host, port, path, headers,
 * query parameters, path parameters, and the request body.
 */
interface RequestObject {
    /**
     * The HTTP method of the request.
     */
    val method: HttpMethod

    /**
     * The host of the request.
     */
    val host: String

    /**
     * The port of the request.
     */
    val port: Int

    /**
     * The path of the request.
     */
    val path: String

    /**
     * The headers of the request.
     */
    val headers: Headers

    /**
     * The query parameters of the request.
     *
     * @return A map of query parameter names to their corresponding values.
     */
    val queryParameters: Map<String, String>

    /**
     * The path parameters of the request.
     *
     * @return A map of path parameter names to their corresponding values.
     */
    val pathParameters: Map<String, String>

    /**
     * The body of the request as a byte array.
     *
     * @return The request body as a byte array.
     */
    val body: ByteArray
}