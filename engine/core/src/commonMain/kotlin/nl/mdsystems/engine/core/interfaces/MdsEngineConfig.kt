@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package nl.mdsystems.engine.core.interfaces

import nl.mdsystems.engine.core.classes.Component
import java.util.concurrent.ConcurrentHashMap

/**
 * An abstract class representing the configuration for the MDS engine.
 * This class provides methods for managing and accessing components in the engine.
 */
expect abstract class MdsEngineConfig {
    /**
     * A concurrent hash map containing all registered components and their associated values.
     */
    abstract val components: ConcurrentHashMap<Component<*>, Any>

    /**
     * Registers a new component with the given value in the engine's configuration.
     *
     * @param component The component to be registered.
     * @param value The value associated with the component.
     */
    fun registerComponent(component: Component<*>, value: Any)

    /**
     * Removes the specified component from the engine's configuration.
     *
     * @param component The component to be removed.
     */
    fun removeComponent(component: Component<*>)

    /**
     * Clears all components from the engine's configuration.
     */
    fun clearComponents()

    /**
     * Retrieves the value associated with the specified component.
     *
     * @param component The component for which the value is to be retrieved.
     * @return The value associated with the component.
     * @throws NoSuchElementException If the component is not found in the configuration.
     */
    fun <T> getComponent(component: Component<T>): T

    /**
     * Retrieves the value associated with the specified component, or null if the component is not found.
     *
     * @param component The component for which the value is to be retrieved.
     * @return The value associated with the component, or null if the component is not found.
     */
    fun <T> getComponentOrNull(component: Component<T>): T?
}