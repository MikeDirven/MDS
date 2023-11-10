package mds.plugins.sessions

import mds.engine.handlers.Application
import mds.engine.plugins.MdsPlugin
import mds.engine.plugins.PluginKey
import mds.plugins.sessions.enums.SessionStorage
import mds.plugins.sessions.interfaces.SessionProvider
import mds.plugins.sessions.interfaces.SessionsConfig

class Sessions(
    val application: Application
) : SessionsConfig {
    override lateinit var sessionProvider: SessionProvider

    companion object: MdsPlugin<Sessions, Sessions>{
        override val key: PluginKey<Sessions> = PluginKey("Sessions")

        override fun install(application: Application, configure: Sessions.() -> Unit): Sessions {
            return Sessions(application).apply(configure)
        }
    }
}