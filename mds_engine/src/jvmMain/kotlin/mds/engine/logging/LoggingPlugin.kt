package mds.engine.logging

import mds.engine.enums.Hook
import mds.engine.handlers.Application
import mds.engine.plugins.MdsPlugin
import mds.engine.plugins.PluginKey

class LoggingPlugin(
    private val application: Application
) {
    init {
        application.on(Hook.BODY_RECEIVED){ call, _  ->
            call?.let {
                println("${Tags.methodColor(it.method)} Request body: ${it.body}")
            }
        }
    }

    companion object : MdsPlugin<LoggingPlugin, LoggingPlugin> {
        override val key: PluginKey<LoggingPlugin> = PluginKey("MdsLoggingPlugin")

        override fun install(application: Application, configure: LoggingPlugin.() -> Unit): LoggingPlugin {
            return LoggingPlugin(application).apply(configure)
        }
    }
}