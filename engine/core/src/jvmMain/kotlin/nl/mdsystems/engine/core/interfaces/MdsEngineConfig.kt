@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package nl.mdsystems.engine.core.interfaces

import nl.mdsystems.engine.core.classes.Component
import java.util.concurrent.ConcurrentHashMap

actual abstract class MdsEngineConfig {
    actual abstract val components: ConcurrentHashMap<Component<*>, Any>

    actual fun registerComponent(component: Component<*>, value: Any) {
        components.put(component, value)
    }

    actual fun removeComponent(component: Component<*>) {
        components.remove(component)
    }

    actual fun clearComponents() {
        components.clear()
    }

    @Suppress("UNCHECKED_CAST")
    actual fun <T> getComponent(component: Component<T>): T {
        return components[component] as? T
            ?: throw IllegalArgumentException("Component $component not found in MdsEngineConfig")
    }

    @Suppress("UNCHECKED_CAST")
    actual fun <T> getComponentOrNull(component: Component<T>): T? {
        return components[component] as? T
    }
}