package nl.mdsystems.engine.core

import nl.mdsystems.engine.core.interfaces.MdsEngineConfig
import java.util.concurrent.atomic.AtomicReference

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal expect abstract class MdsEngine(
    config: MdsEngineConfig.() -> Unit
) {
    internal val engineConfig: MdsEngineConfig

    companion object {
        val instance: AtomicReference<MdsEngine?>
    }
}