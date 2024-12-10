package mds.plugins.sessions.dsl

import mds.engine.classes.HttpCall
import mds.engine.handlers.Routing
import mds.engine.pipelines.subPipelines.ResponsePipeline
import mds.engine.plugins.extensions.plugin
import mds.plugins.sessions.Sessions
import java.util.UUID

inline fun <reified T : Any> ResponsePipeline.getSession() : T {
    val sessionCookie = request.headers.get("Cookie")?.split(';')?.associate {
        val split = it.split('=')
        Pair(split[0], split[1])
    }?.get(applicationPipeline.application.plugin(Sessions).sessionProvider.sessionName) ?: throw InternalError("cannot get cookie from headers !")

    val uuid = UUID.fromString(sessionCookie)

    return applicationPipeline.application.plugin(Sessions).sessionProvider.getSession(T::class, uuid)
}

inline fun <reified T : Any> ResponsePipeline.getSessionOrNull() : T? {
    val sessionCookie = request.headers.get("Cookie")?.split(';')?.associate {
        val split = it.split('=')
        Pair(split[0], split[1])
    }?.get(applicationPipeline.application.plugin(Sessions).sessionProvider.sessionName) ?: throw InternalError("cannot get cookie from headers !")

    val uuid = UUID.fromString(sessionCookie)

    return applicationPipeline.application.plugin(Sessions).sessionProvider.getSessionOrNull(T::class, uuid)
}

inline fun <reified T : Any> ResponsePipeline.setSession(session: T) : UUID {
    val sessionId : UUID

    applicationPipeline.application.plugin(Sessions).sessionProvider.let { provider ->
        sessionId = provider.setSession(T::class, session)
        provider.setSessionUUID(this, sessionId)
    }

    return sessionId
}