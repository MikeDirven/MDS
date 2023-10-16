package mds.engine.interfaces

import mds.engine.factories.RequestThreadFactory
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

interface MdsEngineThreading {
    var maxPoolThreads: Int
    var maxPools: Int
    var threadPools: List<ThreadPoolExecutor>

    fun createThreadPool() = ThreadPoolExecutor(
        maxPoolThreads, maxPoolThreads,
        60L, TimeUnit.SECONDS,
        LinkedBlockingQueue(Int.MAX_VALUE),
        RequestThreadFactory("Mds_Engine_Pool"),
        ThreadPoolExecutor.AbortPolicy()
    )

    fun createThreadPoolList(){
        threadPools = mutableListOf<ThreadPoolExecutor>().apply {
            for(i in 0..(maxPools + 1)){
                this.add(createThreadPool())
            }
        }
    }
}