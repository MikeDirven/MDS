package framework.engine.plugins.extensions

import framework.engine.handlers.Application
import framework.engine.plugins.MdsPlugin
import java.lang.RuntimeException

fun <T : Any> Application.pluginOrNull(plugin: MdsPlugin<*, T>) : T = applicationPlugins.get(plugin.key) as T

fun <T : Any> Application.plugin(plugin: MdsPlugin<*, T>) : T = (applicationPlugins.get(plugin.key) ?: throw RuntimeException("Unable to find plugin instance of: ${plugin.key}"))as T