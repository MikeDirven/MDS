package nl.mdsystems.engine.metrics

import nl.mdsystems.engine.metrics.interfaces.EngineMetrics
import java.util.concurrent.atomic.AtomicLong

class MdsEngineMetrics {
    internal object metrics : EngineMetrics {
        override val totalRequestHandled: AtomicLong = AtomicLong(0L)
        override val totalRequestCurrentlyProcessing: AtomicLong = AtomicLong(0L)

        override val totalRequestProcessingTimeInMs: AtomicLong = AtomicLong(0L)
        override val requestProcessingTimeMaxInMs: AtomicLong = AtomicLong(0L)
        override val requestProcessingTimeMinInMs: AtomicLong = AtomicLong(0L)
        override val requestProcessingTimeAverageInMs: AtomicLong = AtomicLong(0L)
    }

    fun get() = object : EngineMetrics by metrics {}

    fun incrementTotalRequestHandled() {
        metrics.totalRequestHandled.incrementAndGet()
    }


    fun incrementTotalRequestCurrentlyProcessing() {
        metrics.totalRequestCurrentlyProcessing.incrementAndGet()
    }

    fun decrementTotalRequestCurrentlyProcessing() {
        metrics.totalRequestCurrentlyProcessing.decrementAndGet()
    }

    /**
     * Updates the request processing metrics with the given time in milliseconds.
     *
     * This function is responsible for updating the total request handled, total request processing time,
     * maximum request processing time, minimum request processing time, and average request processing time.
     *
     * @param timeInMs The time in milliseconds it took to process the request.
     *
     * @return Nothing. The function updates the internal metrics accordingly.
     */
    fun updateRequestProcessingTimes(timeInMs: Long) {
        incrementTotalRequestHandled()
        metrics.totalRequestProcessingTimeInMs.addAndGet(timeInMs)
        if (timeInMs > metrics.requestProcessingTimeMaxInMs.get()) {
            metrics.requestProcessingTimeMaxInMs.set(timeInMs)
        }
        if (timeInMs < metrics.requestProcessingTimeMinInMs.get() || metrics.requestProcessingTimeMinInMs.get() == 0L) {
            metrics.requestProcessingTimeMinInMs.set(timeInMs)
        }
        val currentTotal = metrics.totalRequestProcessingTimeInMs.get()
        val newTotal = (currentTotal + timeInMs) / metrics.totalRequestHandled.get()
        metrics.requestProcessingTimeAverageInMs.set(newTotal)
    }
}