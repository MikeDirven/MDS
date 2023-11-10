package mds.plugins.sessions.enums

enum class SameSite(val actual: String) {
    STRICT("Strict"),
    LAX("Lax"),
    NONE("None")
}