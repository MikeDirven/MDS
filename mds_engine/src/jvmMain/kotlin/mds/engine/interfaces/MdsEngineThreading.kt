package mds.engine.interfaces

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import mds.engine.factories.RequestThreadFactory
import java.util.concurrent.*

interface MdsEngineThreading {
    var maxPoolThreads: Int
    var maxPools: Int
    var threadPools: List<CoroutineDispatcher>

    fun newCachedThreadPool(maxThreads: Int, threadBuilder: ThreadFactory): ExecutorService {
        return ThreadPoolExecutor(
            0, maxThreads,
            60L, TimeUnit.SECONDS,
            SynchronousQueue(),
            threadBuilder
        )
    }
    fun createDispatcher() = newCachedThreadPool(maxPoolThreads, RequestThreadFactory("MDS_EventPool")).asCoroutineDispatcher().limitedParallelism(maxPoolThreads)

    fun createThreadPoolList(){
        threadPools = buildList {
            for(i in 0..(maxPools + 1)){
                this.add(createDispatcher())
            }
        }
    }
}