package mds.engine.interfaces

import mds.engine.classes.ApplicationHook
import mds.engine.classes.HttpRequest
import mds.engine.classes.HttpResponse
import mds.engine.classes.IdentificationKey
import mds.engine.enums.Hook
import mds.engine.handlers.Application
import mds.engine.pipelines.Pipeline
import java.util.concurrent.atomic.AtomicInteger

interface MdsEngineHooks {
    val applicationHooks: MutableList<ApplicationHook>

    fun on(hook: Hook, block: Application.(Pipeline?, HttpResponse?) -> Unit) = ApplicationHook(
        IdentificationKey("${hook.name}-${hookNumber.getAndIncrement()}"),
        hook,
        block
    ).apply { applicationHooks.add(this) }

    fun on(hook: Hook, name: String, block: Application.(Pipeline?, HttpResponse?) -> Unit) = ApplicationHook(
        IdentificationKey(name),
        hook,
        block
    ).apply { applicationHooks.add(this) }

    companion object {
        private val hookNumber = AtomicInteger(1)
    }
}