package nl.mdsystems.engine.logging

import nl.mdsystems.engine.core.classes.Component
import nl.mdsystems.engine.logging.interfaces.EngineLoggingConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

/**
 * A class that manages logging configuration for the MDS Engine.
 *
 * @param config A lambda function that configures the [EngineLoggingConfig] instance.
 *
 * The [MdsEngineLogging] class initializes an instance of [EngineLoggingConfig] with default values.
 * The provided lambda function [config] is then applied to this instance, allowing customization of the logging configuration.
 *
 * @see EngineLoggingConfig
 */
class MdsEngineLogging(config: (EngineLoggingConfig.() -> Unit)? = null) {
    /**
     * An instance of [EngineLoggingConfig] that holds the logging configuration.
     *
     * The [configuration] instance is initialized with default values, specifically using the [Logger.getGlobal] method.
     * The provided lambda function [config] is then applied to this instance, allowing customization of the logging configuration.
     */
    internal val configuration: EngineLoggingConfig = object : EngineLoggingConfig {
        override val logger: Logger = LoggerFactory.getLogger("MDS")
    }
    
    /**
     * Initializes the [configuration] instance with the provided lambda function [config].
     *
     * This constructor is called when an instance of [MdsEngineLogging] is created.
     * It applies the provided lambda function [config] to the [configuration] instance, allowing customization of the logging configuration.
     */
    init {
        config?.invoke(configuration)

        // Set global instance
        instance.set(this)
    }

    companion object {
        val COMPONENT = Component<MdsEngineLogging>("MDS-Engine-Logging")
        val instance: AtomicReference<MdsEngineLogging?> = AtomicReference<MdsEngineLogging?>(null)

        fun get() = instance.get()

        operator fun getValue(thisRef: Any?, property: KProperty<*>) : MdsEngineLogging {
            return instance.get() ?: MdsEngineLogging().also { instance.set(it) }
        }
    }
}