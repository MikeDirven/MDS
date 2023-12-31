package mds.engine.interfaces

import mds.engine.classes.ContentType
import mds.engine.classes.HttpException
import mds.engine.classes.HttpResponse
import mds.engine.enums.HttpStatusCode
import mds.engine.pipelines.Pipeline
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

typealias ExceptionHandler = (call: Pipeline, cause: Throwable) -> HttpResponse
interface MdsEngineExceptions {
    val exceptionsToCatch: MutableMap<KClass<Throwable>, ExceptionHandler>
    val defaultHandler: (call: Pipeline, cause: Throwable) -> HttpResponse
        get() = { call, cause ->
            HttpResponse(
                HttpStatusCode.INTERNAL_SERVER_ERROR,
                ContentType.Application.JSON,
                mutableListOf(),
                cause.message?.let { HttpException(cause::class.jvmName, it) } ?: "Unknown exception"
            )
        }
}