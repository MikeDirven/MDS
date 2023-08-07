package framework.engine.interfaces

import framework.engine.plugins.PluginKey

interface MdsEnginePlugins {
    val applicationPlugins: MutableMap<PluginKey<*>, Any>

    fun removePlugin(key: PluginKey<*>) {
        applicationPlugins.remove(key)
    }

    fun removePlugin(key: String){
        applicationPlugins.remove(PluginKey<Any>(key))
    }
}