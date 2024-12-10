package nl.mdsystems.engine.modules

import kotlinx.coroutines.*
import nl.mdsystems.engine.core.MdsEngine
import nl.mdsystems.engine.core.MdsEngine.Companion.getValue
import nl.mdsystems.engine.core.classes.Component
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.MdsEngineLogging.Companion.getValue
import nl.mdsystems.engine.logging.functions.error
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.modules.classes.MdsModule
import nl.mdsystems.engine.modules.interfaces.EngineModulesConfig
import nl.mdsystems.utils.forEachAsync
import java.io.File
import java.net.URLClassLoader
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import java.util.jar.JarFile
import kotlin.reflect.KProperty

class MdsEngineModules(config: (EngineModulesConfig.() -> Unit)? = null) {
    private val engine by MdsEngine
    private val logger by MdsEngineLogging
    private val dispatcher = CoroutineScope(Dispatchers.IO)
    private var classLoader: URLClassLoader
    private val installedExternalModules: ConcurrentHashMap<String, MdsModule> = ConcurrentHashMap()
    private val configuration = object : EngineModulesConfig {
        override var waitForModules: Boolean = false
        override var externalModulesDirectories: List<File> = emptyList()
        override var dependenciesDirectories: List<File> = emptyList()
        override var internalModules: List<MdsModule> = emptyList()
    }

    init {
        config?.invoke(configuration)

        // Search directories for dependency files and add them to the classloader

        classLoader = URLClassLoader(
            configuration.dependenciesDirectories.flatMap { directory ->
                directory.walkTopDown()
                    .filter { it.isFile && it.extension == "jar" }.map {
                        it.toURI().toURL()
                    }.toList()
            }.toTypedArray(),
            ClassLoader.getSystemClassLoader()
        )

        logger.info(
            """ Modules loader started,
                Internal modules to load: ${configuration.internalModules.count()}
                External directories to search: ${configuration.externalModulesDirectories.count()}
            """.trimIndent()
        )
    }

    fun loadModules() {
        if (configuration.waitForModules) {
            logger.info("Waiting for modules to be loaded...")
            runBlocking {
                loadInternalModules()
                loadExternalModules()
            }
        } else {
            dispatcher.launch() {
                loadInternalModules()
                loadExternalModules()
            }
        }
    }

    suspend fun loadInternalModules() {
        configuration.internalModules.forEachAsync { module ->
            try {
                module.entry.invoke(engine.engineConfig)
                logger.info("loaded module ${module.name}")
            } catch (e: Exception) {
                logger.error("Failed to load module ${module.name}")
                throw e
            }
        }
    }

    fun loadExternalModules() {
        runBlocking {
            val loaders = configuration.externalModulesDirectories.flatMap { directory ->
                directory.listFiles { file ->
                    file.isFile
                            && file.extension == "jar"
                            && !installedExternalModules.containsKey(getModuleName(file)) // Filter already installed modules
                }?.toList() ?: listOf()
            }

            classLoader = URLClassLoader(loaders.map { it.toURI().toURL() }.toTypedArray(), classLoader)

            val jobs = loaders.map { jar ->
                launch {
                    loadExternalModule(jar)
                }
            }

            jobs.joinAll()
        }
    }

    private fun getModuleName(jar: File): String? {
        val moduleJar = JarFile(jar)
        return moduleJar.manifest?.mainAttributes?.getValue("Module-Name") // Assuming module name is in Implementation-Title
            ?: moduleJar.manifest?.mainAttributes?.getValue("Main-Class")
    }

    private suspend fun loadExternalModule(jar: File) {
        val moduleJar = withContext(Dispatchers.IO) {
            JarFile(jar)
        }
        val moduleName = getModuleName(jar) ?: return // Skip if module name cannot be determined

        val moduleClass = moduleJar.manifest?.mainAttributes?.getValue("Main-Class")
            ?: throw ClassNotFoundException("No main class found in ${jar.name}, Check the manifest if the main class has been set.")

        val moduleVersion = moduleJar.manifest?.mainAttributes?.getValue("Version") ?: "UNKNOWN"

        // Get The first method that return a KtorModule instance
        val initializer = try {
            classLoader.loadClass(moduleClass).methods.firstOrNull { method ->
                method.returnType == MdsModule::class.java
            }?.invoke(null) as? MdsModule
                ?: throw InstantiationException("Module $moduleName does not implement KtorModule method.")
        } catch (e: Exception) {
            logger.error("Error loading module $moduleName: ${e.message}")
            logger.error(e)
            null
        }

        initializer?.let { moduleInstance ->
            moduleInstance.entry(engine.engineConfig)
            installedExternalModules[moduleName] = moduleInstance // Store by module name
            logger.info("Module: $moduleClass installed with version: $moduleVersion")
        }
    }

    companion object {
        val COMPONENT = Component<MdsEngineModules>("MDS-Engine-Modules")

        val instance: AtomicReference<MdsEngineModules?> = AtomicReference<MdsEngineModules?>(null)

        fun get() = instance.get()

        operator fun getValue(thisRef: Any?, property: KProperty<*>) : MdsEngineModules? {
            return instance.get()
        }
    }
}