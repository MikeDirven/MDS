package framework.engine.interfaces

import framework.engine.handlers.Application
import framework.engine.logging.Tags
import framework.engine.plugins.MdsPlugin

inline fun <reified C : Any, reified P: MdsPlugin<C, *>> Application.install(plugin: P, noinline config: C.() -> Unit = {}){
    if (applicationPlugins.containsKey(plugin.key))
        println("${Tags.engine} Plugin installer - ${plugin.key.name} already exists!")
    else {
        applicationPlugins[plugin.key] = plugin.install(this, config)
        println("${Tags.engine} Plugin installer - ${plugin.key.name} installed!")
    }
}