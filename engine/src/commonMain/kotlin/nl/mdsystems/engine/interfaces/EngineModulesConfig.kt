package nl.mdsystems.engine.interfaces

import nl.mdsystems.engine.MdsModule
import java.io.File

interface EngineModulesConfig {
    var waitForModules: Boolean
    var externalModulesDirectories: List<File>
    var dependenciesDirectories: List<File>
    var internalModules: List<MdsModule>

    fun directories(vararg directories: File) {
        externalModulesDirectories = buildList {
            addAll(externalModulesDirectories)
            addAll(directories)
        }
    }

    fun directories(directories: List<File>) {
        externalModulesDirectories = buildList {
            addAll(externalModulesDirectories)
            addAll(directories)
        }
    }

    fun addDirectory(directory: File) {
        externalModulesDirectories = buildList {
            addAll(externalModulesDirectories)
            add(directory)
        }
    }

    fun dependenciesDirectories(vararg directories: File) {
        dependenciesDirectories = buildList {
            addAll(dependenciesDirectories)
            addAll(directories)
        }
    }

    fun dependenciesDirectories(directories: List<File>) {
        dependenciesDirectories = buildList {
            addAll(dependenciesDirectories)
            addAll(directories)
        }
    }

    fun addDependencyDirectory(directory: File) {
        dependenciesDirectories = buildList {
            addAll(dependenciesDirectories)
            add(directory)
        }
    }

    operator fun EngineModulesConfig.plus(directory: File) {
        externalModulesDirectories = buildList {
            addAll(externalModulesDirectories)
            add(directory)
        }
    }

    infix fun EngineModulesConfig.add(directory: File) {
        externalModulesDirectories = buildList {
            addAll(externalModulesDirectories)
            add(directory)
        }
    }

    fun modules(vararg modulesToAdd: MdsModule) {
        internalModules = buildList {
            addAll(internalModules)
            addAll(modulesToAdd)
        }
    }

    fun modules(modulesToAdd: List<MdsModule>) {
        internalModules = buildList {
            addAll(internalModules)
            addAll(modulesToAdd)
        }
    }

    fun addModule(module: MdsModule) {
        internalModules = buildList {
            addAll(internalModules)
            add(module)
        }
    }

    operator fun MdsModule.unaryPlus(){
        internalModules = buildList {
            addAll(internalModules)
            add(this@unaryPlus)
        }
    }

    infix fun EngineModulesConfig.add(module: MdsModule){
        internalModules = buildList {
            addAll(internalModules)
            add(module)
        }
    }
}