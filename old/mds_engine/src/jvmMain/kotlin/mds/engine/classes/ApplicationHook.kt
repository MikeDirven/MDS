package mds.engine.classes

import mds.engine.enums.Hook
import mds.engine.handlers.Application
import mds.engine.pipelines.Pipeline

class ApplicationHook(
    val key: IdentificationKey,
    val hook: Hook,
    val function: Application.(call: Pipeline?, response: HttpResponse?) -> Unit
)