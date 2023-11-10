package mds.plugins.sessions.interfaces

import mds.plugins.sessions.enums.SameSite
import java.util.*

interface SessionCookieConfig {
    var domain: String
    var expires: Date
    var httpOnly: Boolean
    var maxAgeInSeconds: Int
    var path: String
    var sameSite: SameSite
    var secure: Boolean
}