package nl.mdsystems.utils

import java.text.DecimalFormat
import java.util.concurrent.atomic.AtomicLong

val initializeTimeStamp: AtomicLong = AtomicLong(System.currentTimeMillis())

fun setStartingTime() = initializeTimeStamp.set(System.currentTimeMillis())

fun getElapsedTimeInSecondsWithDecimal(): String {
    val currentTimeMillis = System.currentTimeMillis()
    val elapsedTimeMillis = currentTimeMillis - initializeTimeStamp.get()
    val elapsedTimeInSeconds = elapsedTimeMillis / 1000.0

    val decimalFormat = DecimalFormat("0.0000")
    return decimalFormat.format(elapsedTimeInSeconds)
}