package mds.plugins.sessions.interfaces

import mds.plugins.sessions.enums.SameSite
import java.io.File
import java.util.*

interface FileSessionCookieConfig {
    var fileLocation: File

    var domain: String
    var expires: Date
    var httpOnly: Boolean
    var maxAgeInSeconds: Int
    var path: String
    var sameSite: SameSite
    var secure: Boolean
}