package logging

import framework.engine.enums.Hook
import framework.engine.handlers.Application
import framework.engine.plugins.MdsPlugin
import framework.engine.plugins.PluginKey

class LoggingPlugin(
    val application: Application
) {
    init {
        application.on(Hook.BODY_RECEIVED){ call ->
            call?.let {
                println("${Tags.methodColor(it.method)} ${it.body}")
            }
        }
    }

    companion object : MdsPlugin<LoggingPlugin, LoggingPlugin>{
        override val key: PluginKey<LoggingPlugin> = PluginKey("MdsLoggingPlugin")

        override fun install(application: Application, configure: LoggingPlugin.() -> Unit): LoggingPlugin {
            return LoggingPlugin(application).apply(configure)
        }
    }
}