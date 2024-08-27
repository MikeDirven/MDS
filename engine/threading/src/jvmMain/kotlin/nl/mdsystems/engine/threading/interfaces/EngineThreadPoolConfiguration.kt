package nl.mdsystems.engine.threading.interfaces

/**
 * Interface defining the configuration for an engine thread pool.
 *
 * This interface provides properties to configure the thread pool's name, maximum number of pools,
 * and maximum number of threads per pool.
 */
interface EngineThreadPoolConfiguration {
    var poolName: String
    var maxPools: Int
    var maxPoolThreads: Int
}