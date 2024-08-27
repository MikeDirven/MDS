package nl.mdsystems.engine.threading

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.functions.info
import nl.mdsystems.engine.threading.interfaces.EngineThreadPoolConfiguration
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * A class responsible for managing a set of thread pools for the MdsEngine.
 *
 * @param config A lambda function that configures the thread pool settings.
 *
 * The configuration settings include:
 * - [EngineThreadPoolConfiguration.maxPoolThreads]: The maximum number of threads in each pool.
 * - [EngineThreadPoolConfiguration.maxPools]: The maximum number of pools to create.
 * - [EngineThreadPoolConfiguration.poolName]: The base name for the thread pools.
 *
 * The class initializes the thread pools and provides a method to select the least busy pool.
 */
@OptIn(DelicateCoroutinesApi::class)
class MdsEngineThreading(config: (EngineThreadPoolConfiguration.() -> Unit)? = null) {
    private val configuration = object : EngineThreadPoolConfiguration {
        override var maxPoolThreads: Int = 4
        override var maxPools: Int = 10
        override var poolName: String = "MdsEngineThreadPool"
    }

    private val threadPools = CopyOnWriteArrayList<ExecutorCoroutineDispatcher>()

    private val workloadQueue = PriorityQueue<Pair<ExecutorCoroutineDispatcher, Int>>(compareBy { it.second })

    /**
     * Selects and returns the least busy thread pool from the available pools.
     *
     * This function polls the workload queue to find the thread pool with the lowest workload.
     * If a thread pool is found, its workload is incremented by 1 and the dispatcher is returned.
     * If no thread pools are available, an [IllegalStateException] is thrown.
     *
     * @return The least busy [ExecutorCoroutineDispatcher] from the available thread pools.
     *
     * @throws IllegalStateException If no available thread pools are found.
     */
    fun selectLeastBusyPool(): ExecutorCoroutineDispatcher {
        val entry = workloadQueue.poll()
        if (entry != null) {
            updateWorkload(entry.first, entry.second + 1)
            return entry.first
        }
        throw IllegalStateException("No available thread pools")
    }

    fun closeAllPools() {
        MdsEngineLogging.get()?.info("Closing all thread pools")

        threadPools.forEach { it.close() }
        threadPools.clear()
        workloadQueue.clear()

        MdsEngineLogging.get()?.info("All thread pools closed")
    }

    /**
     * Updates the workload of a specific thread pool in the workload queue.
     *
     * This function removes the existing entry for the given [dispatcher] from the workload queue,
     * then adds a new entry with the updated [workload].
     *
     * @param dispatcher The [ExecutorCoroutineDispatcher] representing the thread pool whose workload needs to be updated.
     * @param workload The new workload value for the specified thread pool.
     */
    private fun updateWorkload(dispatcher: ExecutorCoroutineDispatcher, workload: Int) {
        workloadQueue.removeIf { it.first === dispatcher }
        workloadQueue.add(dispatcher to workload)
    }

    init {
        config?.invoke(configuration)

        MdsEngineLogging.get()?.info(
            "Initializing thread pools, amount of pools: ${configuration.maxPools}"
        )

        for (poolNumber in 0 until configuration.maxPools) {
            val dispatcher = newFixedThreadPoolContext(
                configuration.maxPoolThreads,
                configuration.poolName + "-$poolNumber"
            )
            threadPools.add(dispatcher)
            updateWorkload(dispatcher, 0) // Initialize workload as 0
        }
        MdsEngineLogging.get()?.info("Thread pools initialized")
    }
}