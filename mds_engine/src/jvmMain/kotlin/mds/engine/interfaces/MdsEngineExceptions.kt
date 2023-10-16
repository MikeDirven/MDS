package mds.engine.interfaces

import mds.engine.classes.ContentType
import mds.engine.classes.HttpException
import mds.engine.classes.HttpRequest
import mds.engine.classes.HttpResponse
import mds.engine.enums.HttpStatusCode
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

typealias ExceptionHandler = (call: HttpRequest, cause: Exception) -> HttpResponse
interface MdsEngineExceptions {
    val exceptionsToCatch: MutableMap<KClass<Exception>, ExceptionHandler>
    val defaultHandler: (call: HttpRequest, cause: Exception) -> HttpResponse
        get() = { call, cause ->
            HttpResponse(
                HttpStatusCode.INTERNAL_SERVER_ERROR,
                ContentType.Application.JSON,
                cause.message?.let { HttpException(cause::class.jvmName, it) } ?: "Unknown exception"
            )
        }
}