package framework.engine.classes

import framework.engine.enums.Hook
import framework.engine.handlers.Application

class ApplicationHook(
    val key: IdentificationKey,
    val hook: Hook,
    val function: Application.(call: HttpRequest?) -> Unit
)