package nl.mdsystems.engine.modules.classes

import nl.mdsystems.engine.core.MdsEngine
import nl.mdsystems.engine.modules.types.MdsEntry

/**
 * Represents a module in the MDS engine.
 *
 * @param name The name of the module. If not provided, it will default to the name of the function passed as [entry].
 * @param entry The entry point of the module, represented as a lambda function that takes an instance of [MdsEngine] as a receiver.
 *
 * @constructor Creates a new instance of [MdsModule].
 */
class MdsModule internal constructor(
    val name: String = "",
    internal val entry: MdsEntry
)

/**
 * Creates a new instance of [MdsModule].
 *
 * This function is used to define a module in the MDS engine. It takes two parameters:
 * - [name]: The name of the module. If not provided, it will default to the name of the function passed as [entry].
 * - [entry]: The entry point of the module, represented as a lambda function that takes an instance of [MdsEngine] as a receiver.
 *
 * @return An instance of [MdsModule] with the provided [name] and [entry] function.
 *
 * @sample
 * ```
 * val myModule = mdsModule("MyModule") {
 *     // Module logic goes here
 * }
 * ```
 */
fun mdsModule(name: String = "", entry: MdsEntry): MdsModule {
    return MdsModule(
        name.ifBlank { entry.toString().substringBefore("$$") },
        entry
    )
}