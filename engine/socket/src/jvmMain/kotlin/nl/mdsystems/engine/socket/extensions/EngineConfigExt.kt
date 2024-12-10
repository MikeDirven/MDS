package nl.mdsystems.engine.socket.extensions

import nl.mdsystems.engine.core.interfaces.MdsEngineConfig
import nl.mdsystems.engine.socket.MdsEngineHttpSocket
import nl.mdsystems.engine.socket.interfaces.EngineSocketConfig

/**
 * Configures and registers the [MdsEngineHttpSocket] component for the MDS Engine.
 *
 * This function is used to create an instance of [MdsEngineHttpSocket] and register it as a component in the MDS Engine.
 * The provided [configure] function is used to customize the configuration of the [MdsEngineHttpSocket] instance.
 *
 * @param configure A function that takes an instance of [EngineSocketConfig] and configures it.
 *
 * @return This function does not return a value. It registers the configured [MdsEngineHttpSocket] instance as a component in the MDS Engine.
 *
 * @see MdsEngineHttpSocket
 * @see EngineSocketConfig
 * @see MdsEngineConfig.registerComponent
 */
fun MdsEngineConfig.socket(configure: EngineSocketConfig.() -> Unit) {
    val instance = MdsEngineHttpSocket(configure)
    registerComponent(MdsEngineHttpSocket.COMPONENT, instance)
}