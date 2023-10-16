package mds.engine.factories

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class RequestThreadFactory(poolName: String) : ThreadFactory {
    private val group: ThreadGroup = Thread.currentThread().threadGroup
    private val threadNumber = AtomicInteger(1)
    private val namePrefix: String = "$poolName-" +
            poolNumber.getAndIncrement() + "-thread-"

    override fun newThread(r: Runnable): Thread {
        val t = Thread(
            group, r,
            namePrefix + threadNumber.getAndIncrement(),
            0
        )
        if (t.isDaemon) t.isDaemon = false
        if (t.priority != Thread.NORM_PRIORITY) t.priority = Thread.NORM_PRIORITY
        return t
    }

    companion object {
        private val poolNumber = AtomicInteger(1)
    }
}
