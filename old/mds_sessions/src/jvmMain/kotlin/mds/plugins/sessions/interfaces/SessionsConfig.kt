package mds.plugins.sessions.interfaces

import mds.plugins.sessions.enums.SessionStorage

interface SessionsConfig {
    val sessionProvider: SessionProvider
}