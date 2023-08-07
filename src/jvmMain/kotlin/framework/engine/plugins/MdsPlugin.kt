package framework.engine.plugins

import framework.engine.handlers.Application

interface MdsPlugin<
        out Configuration: Any,
        Plugin: Any
        > {

    val key: PluginKey<Plugin>

    fun install(application: Application, configure: Configuration.() -> Unit) : Plugin
}