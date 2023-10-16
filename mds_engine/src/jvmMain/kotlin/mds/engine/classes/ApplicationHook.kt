package mds.engine.classes

import mds.engine.enums.Hook
import mds.engine.handlers.Application

class ApplicationHook(
    val key: IdentificationKey,
    val hook: Hook,
    val function: Application.(call: HttpRequest?, response: HttpResponse?) -> Unit
)