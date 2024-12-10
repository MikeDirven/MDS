package mds.engine.interfaces

import mds.engine.handlers.Application
import mds.engine.plugins.MdsPlugin
import mds.engine.logging.Tags
import mds.engine.logging.extensions.info

inline fun <reified C : Any, reified P: MdsPlugin<C, *>> Application.install(plugin: P, noinline config: C.() -> Unit = {}){
    if (applicationPlugins.containsKey(plugin.key))
        info("${Tags.engine} Plugin installer - ${plugin.key.name} already exists!")
    else {
        applicationPlugins[plugin.key] = plugin.install(this, config)
        info("${Tags.engine} Plugin installer - ${plugin.key.name} installed!")
    }
}