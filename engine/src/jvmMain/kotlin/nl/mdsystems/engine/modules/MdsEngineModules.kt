package nl.mdsystems.engine.modules

import nl.mdsystems.engine.MdsModule
import nl.mdsystems.engine.interfaces.EngineModulesConfig
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.functions.info
import java.io.File

class MdsEngineModules(config: (EngineModulesConfig.() -> Unit)? = null) {
    private val configuration = object : EngineModulesConfig {
        override var waitForModules: Boolean = false
        override var externalModulesDirectories: List<File> = emptyList()
        override var internalModules: List<MdsModule> = emptyList()
    }

    init {
        config?.invoke(configuration)

        MdsEngineLogging.get()?.info(
            """ Modules loader started,
                Internal modules to load: ${configuration.internalModules.count()}
                External directories to search: ${configuration.externalModulesDirectories.count()}
            """.trimIndent()
        )

        if(configuration.waitForModules) MdsEngineLogging.get()?.info(
            "Waiting for modules to be loaded..."
        )
    }
}