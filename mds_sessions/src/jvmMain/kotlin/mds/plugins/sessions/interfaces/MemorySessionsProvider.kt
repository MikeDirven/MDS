package mds.plugins.sessions.interfaces

import mds.engine.classes.HttpCall
import mds.engine.classes.HttpHeader
import mds.plugins.sessions.enums.SameSite
import mds.plugins.sessions.exceptions.SessionExceptions
import java.util.*
import kotlin.reflect.KClass

class MemorySessionsProvider(override val sessionName: String, cookieConfig: SessionCookieConfig.() -> Unit) : SessionProvider, SessionCookieConfig {
    val sessions: MutableMap<KClass<*>, MutableMap<UUID, Any>> = mutableMapOf()
    override lateinit var domain: String
    override lateinit var expires: Date
    override var httpOnly: Boolean = false
    override var maxAgeInSeconds: Int = 3600
    override var path: String = "/"
    override var sameSite: SameSite = SameSite.LAX
    override var secure: Boolean = false

    init {
        cookieConfig()
    }

    fun clearSessions() = sessions.clear()

    override fun <T : Any> getSession(clazz: KClass<T>, uuid: UUID): T {
        return sessions[clazz]?.let { storage ->
            if(storage.containsKey(uuid)){
                storage[uuid] as T
            } else {
                throw SessionExceptions.SessionNotFound(uuid)
            }
        } ?: throw SessionExceptions.UnknownSessionType(clazz)
    }

    override fun <T : Any> getSessionOrNull(clazz: KClass<T>, uuid: UUID): T? {
        return sessions[clazz]?.let { storage ->
            if(storage.containsKey(uuid)){
                storage[uuid] as T
            } else {
                null
            }
        }
    }

    override fun <T : Any> setSession(Clazz: KClass<T>, session: T): UUID {
        val uuid = UUID.randomUUID()
        sessions[Clazz]?.put(uuid, session) ?: sessions.put(Clazz, mutableMapOf(uuid to session))
        return uuid
    }

    override fun setSessionUUID(call: HttpCall, uuid: UUID) {
        val cookieBuilder: StringBuilder = java.lang.StringBuilder()
        cookieBuilder.append("${sessionName}=${uuid.toString()}")
        if(this::domain.isInitialized) cookieBuilder.append("; Domain=${this.domain}")
        if(this::expires.isInitialized) cookieBuilder.append("; expires=${this.expires.toInstant().toString()}")
        if(this.httpOnly) cookieBuilder.append("; HttpOnly")
        cookieBuilder.append("; Max-Age=${this.maxAgeInSeconds}")
        cookieBuilder.append("; Path=${this.path}")
        cookieBuilder.append("; SameSite=${this.sameSite.actual}")
        if(this.secure) cookieBuilder.append("; Secure")

        call.response.headers.add(
            HttpHeader(
                "Set-Cookie",
                cookieBuilder.toString()
            )
        )
    }
}