package nl.mdsystems.engine.pipelines.objects

import com.sun.net.httpserver.Headers
import nl.mdsystems.engine.pipelines.enums.HttpMethod
import java.io.OutputStreamWriter

/**
 * Represents an HTTP response object.
 *
 * This interface provides methods and properties to manipulate an HTTP response.
 * It includes the headers and body of the response.
 *
 * @property headers The headers of the HTTP response.
 * @property body The body of the HTTP response as a byte array.
 */
interface ResponseObject {
    val headers: Headers
    var body: ByteArray
}