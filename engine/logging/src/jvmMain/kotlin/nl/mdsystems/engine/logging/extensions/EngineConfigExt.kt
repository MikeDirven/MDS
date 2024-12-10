package nl.mdsystems.engine.logging.extensions

import nl.mdsystems.engine.core.interfaces.MdsEngineConfig
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.interfaces.EngineLoggingConfig

/**
 * Configures and registers the MdsEngineLogging instance with the provided [MdsEngineConfig].
 *
 * This function is used to initialize and customize the logging functionality of the MDS engine.
 * It takes a lambda function [configure] as a parameter, which is used to configure the [EngineLoggingConfig].
 *
 * @param configure A lambda function that configures the [EngineLoggingConfig].
 *
 * @return This function does not return any value. It registers the configured [MdsEngineLogging] instance with the provided [MdsEngineConfig].
 */
fun MdsEngineConfig.logging(configure: EngineLoggingConfig.() -> Unit){
    val instance = MdsEngineLogging(configure)
    registerComponent(MdsEngineLogging.COMPONENT, instance)
}