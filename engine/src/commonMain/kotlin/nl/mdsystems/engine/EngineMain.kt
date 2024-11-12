package nl.mdsystems.engine

expect class EngineMain

class MdsModule internal constructor(
    val name: String = "",
    internal val entry: EngineMain.() -> Unit
)

fun mdsModule(name: String = "", entry: EngineMain.() -> Unit) : MdsModule {
    return MdsModule(
        name,
        entry
    )
}