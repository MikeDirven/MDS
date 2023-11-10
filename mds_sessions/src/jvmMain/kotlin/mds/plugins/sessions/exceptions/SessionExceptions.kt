package mds.plugins.sessions.exceptions

import java.util.UUID
import kotlin.reflect.KClass

class SessionExceptions{
    class UnknownSessionType(klass: KClass<*>) : Exception("Session storage not found for class: ${klass.simpleName}")
    class SessionNotFound(id: UUID) : Exception("Session for id: ${id.toString()} is not found!")
}
