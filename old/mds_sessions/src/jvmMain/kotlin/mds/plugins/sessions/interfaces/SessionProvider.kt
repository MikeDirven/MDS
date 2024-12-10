package mds.plugins.sessions.interfaces

import mds.engine.classes.HttpCall
import mds.engine.pipelines.subPipelines.ResponsePipeline
import java.util.UUID
import kotlin.reflect.KClass

interface SessionProvider {
    val sessionName: String
    fun <T : Any> getSession(clazz: KClass<T>, uuid: UUID) : T {
        TODO("Not yet implemented")
    }

    fun <T : Any> getSessionOrNull(clazz: KClass<T>, uuid: UUID) : T? {
        TODO("Not yet implemented")
    }

    fun <T : Any> setSession(Clazz: KClass<T>, session: T) : UUID {
        TODO("Not yet implemented")
    }

    fun setSessionUUID(call: ResponsePipeline, uuid: UUID){
        TODO("Not yet implemented")
    }
}