package nl.mdsystems.engine.logging.interfaces

import org.slf4j.Logger


/**
 * An interface that defines the configuration parameters for logging in the MDS Engine.
 *
 * @property logger The [Logger] instance used for logging.
 *
 * The [EngineLoggingConfig] interface provides a contract for classes that need to configure logging for the MDS Engine.
 * It includes a single property, [logger], which represents the [Logger] instance used for logging.
 *
 * Implementations of this interface can customize the [logger] property to use different logging frameworks or configurations.
 *
 * @see Logger
 */
interface EngineLoggingConfig {
    val logger: Logger
}