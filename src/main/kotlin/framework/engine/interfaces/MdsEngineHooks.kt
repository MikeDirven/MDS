package framework.engine.interfaces

import framework.engine.classes.ApplicationHook
import framework.engine.classes.HttpRequest
import framework.engine.classes.IdentificationKey
import framework.engine.enums.Hook
import framework.engine.handlers.Application
import java.util.concurrent.atomic.AtomicInteger

interface MdsEngineHooks {
    val applicationHooks: MutableList<ApplicationHook>

    fun on(hook: Hook, block: Application.(HttpRequest?) -> Unit) = ApplicationHook(
        IdentificationKey("${hook.name}-${hookNumber.getAndIncrement()}"),
        hook,
        block
    ).apply { applicationHooks.add(this) }

    fun on(hook: Hook, name: String, block: Application.(HttpRequest?) -> Unit) = ApplicationHook(
        IdentificationKey(name),
        hook,
        block
    ).apply { applicationHooks.add(this) }

    companion object {
        private val hookNumber = AtomicInteger(1)
    }
}