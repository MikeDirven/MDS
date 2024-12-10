package nl.mdsystems.engine.core

import nl.mdsystems.engine.core.classes.Component
import nl.mdsystems.engine.core.interfaces.MdsEngineConfig
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual abstract class MdsEngine actual constructor(
    config: MdsEngineConfig.() -> Unit
) : Thread("Mds_Engine_Thread") {
    actual val engineConfig: MdsEngineConfig = object : MdsEngineConfig() {
        override val components: ConcurrentHashMap<Component<*>, Any>
            = ConcurrentHashMap()
    }

    init {
        try {
            engineConfig.apply(config)

            instance.set(this)
        } catch (e: Exception){
            throw RuntimeException("Failed to initialize Mds Engine", e)
        }
    }

    actual companion object {
        actual val instance: AtomicReference<MdsEngine?> = AtomicReference(null)

        fun get() = instance.get()

        operator fun getValue(thisRef: Any?, property: KProperty<*>) : MdsEngine {
            return instance.get() ?: throw InstantiationException("Socket not yet initialized!")
        }
    }
}

