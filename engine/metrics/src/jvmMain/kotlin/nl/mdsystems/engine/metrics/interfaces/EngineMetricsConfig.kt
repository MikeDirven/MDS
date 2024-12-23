package nl.mdsystems.engine.metrics.interfaces

import java.util.concurrent.atomic.AtomicLong

interface EngineMetricsConfig {
    val totalRequestHandled: AtomicLong
    val totalRequestCurrentlyProcessing: AtomicLong

    val totalRequestProcessingTimeInMs: AtomicLong
    val requestProcessingTimeMaxInMs: AtomicLong
    val requestProcessingTimeMinInMs: AtomicLong
    val requestProcessingTimeAverageInMs: AtomicLong
}