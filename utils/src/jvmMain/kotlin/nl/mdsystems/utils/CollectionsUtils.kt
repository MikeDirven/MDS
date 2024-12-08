package nl.mdsystems.utils

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext


/**
 * Executes the given [block] function asynchronously on each element of the iterable collection.
 * The execution of the [block] function for each element is performed concurrently using coroutines.
 *
 * @param block a suspend function that takes a single parameter of type [V] and returns [Unit].
 * This function will be invoked for each element of the iterable collection.
 *
 * @return [Unit] - This function does not return a value.
 *
 * @throws CancellationException if the computation is cancelled.
 * @throws InterruptedException if the current thread is interrupted during the computation.
 * @throws TimeoutException if the computation does not complete within the specified timeout.
 */
suspend fun <V> Iterable<V>.forEachAsync(block: suspend (V) -> Unit): Unit = coroutineScope {
    val blocks : MutableList<Deferred<*>> = mutableListOf()

    forEachIndexed { index, entry ->
        blocks.add(
            index,
            async(Dispatchers.IO) { block(entry) }
        )
    }

    withContext(Dispatchers.Default) {
        blocks.awaitAll()
    }
}

/**
 * Executes the given [block] function asynchronously on each element of the iterable collection,
 * including the index of the element. The execution of the [block] function for each element is
 * performed concurrently using coroutines.
 *
 * @param block a suspend function that takes two parameters:
 * - [index]: an [Int] representing the index of the current element in the iterable collection.
 * - [V]: the type of the current element in the iterable collection.
 * This function will be invoked for each element of the iterable collection.
 *
 * @return [Unit] - This function does not return a value.
 *
 * @throws CancellationException if the computation is cancelled.
 * @throws InterruptedException if the current thread is interrupted during the computation.
 * @throws TimeoutException if the computation does not complete within the specified timeout.
 */
suspend fun <V> Iterable<V>.forEachAsyncIndexed(block: suspend (Int, V) -> Unit): Unit = coroutineScope {
    val blocks : MutableList<Deferred<*>> = mutableListOf()

    forEachIndexed { index, entry ->
        blocks.add(
            index,
            async(Dispatchers.IO) { block(index, entry) }
        )
    }

    withContext(Dispatchers.Default) {
        blocks.awaitAll()
    }
}

/**
 * Executes the given [block] function asynchronously on each entry of the map.
 * The execution of the [block] function for each entry is performed concurrently using coroutines.
 *
 * @param block a suspend function that takes two parameters:
 * - [K]: the type of the key in the map.
 * - [V]: the type of the value in the map.
 * This function will be invoked for each entry of the map.
 *
 * @return [Unit] - This function does not return a value.
 *
 * @throws CancellationException if the computation is cancelled.
 * @throws InterruptedException if the current thread is interrupted during the computation.
 * @throws TimeoutException if the computation does not complete within the specified timeout.
 */
suspend fun <K, V> Map<K, V>.forEachAsync(block: suspend (K, V) -> Unit): Unit = coroutineScope {
    val blocks : MutableList<Deferred<*>> = mutableListOf()

    onEachIndexed{ index, entry ->
        blocks.add(
            index,
            async(Dispatchers.IO) { block(entry.key, entry.value) }
        )
    }

    withContext(Dispatchers.Default) {
        blocks.awaitAll()
    }
}

/**
 * Performs an asynchronous transformation on each element of the iterable collection,
 * returning a list of the results.
 *
 * This function is designed to be used with suspend functions, allowing for non-blocking
 * execution of the transformation. The transformation is performed concurrently on each
 * element using coroutines, with the [Dispatchers.IO] dispatcher.
 *
 * @param transform A suspend function that takes an element of the iterable collection
 * and returns a transformed result.
 *
 * @return A list of the results of applying the [transform] function to each element
 * of the iterable collection. The order of the results is the same as the order of the
 * elements in the original collection.
 *
 * @throws CancellationException If the coroutine is cancelled.
 * @throws InterruptedException If the coroutine is interrupted.
 * @throws TimeoutException If the coroutine times out.
 * @throws Exception If an exception is thrown during the transformation.
 */
suspend fun <V, R> Iterable<V>.mapAsync(transform: suspend (V) -> R): List<R> = coroutineScope {
    val mapJobs : MutableList<Deferred<R>> = mutableListOf()

    forEachIndexed { index, entry ->
        mapJobs.add(
            index,
            async(Dispatchers.IO) { transform(entry) }
        )
    }

    withContext(Dispatchers.Default) {
        mapJobs.awaitAll()
    }
}

/**
 * Performs an asynchronous transformation on each element of the iterable collection,
 * returning a list of the results, including the index of each element.
 *
 * This function is designed to be used with suspend functions, allowing for non-blocking
 * execution of the transformation. The transformation is performed concurrently on each
 * element using coroutines, with the [Dispatchers.IO] dispatcher.
 *
 * @param transform A suspend function that takes the index and an element of the iterable collection
 * and returns a transformed result.
 *
 * @return A list of the results of applying the [transform] function to each element
 * of the iterable collection. The order of the results is the same as the order of the
 * elements in the original collection.
 *
 * @throws CancellationException If the coroutine is cancelled.
 * @throws InterruptedException If the coroutine is interrupted.
 * @throws TimeoutException If the coroutine times out.
 * @throws Exception If an exception is thrown during the transformation.
 */
suspend fun <V, R> Iterable<V>.mapAsyncIndexed(transform: suspend (Int, V) -> R): List<R> = coroutineScope {
    val mapJobs : MutableList<Deferred<R>> = mutableListOf()

    forEachIndexed { index, entry ->
        mapJobs.add(
            index,
            async(Dispatchers.IO) { transform(index, entry) }
        )
    }

    withContext(Dispatchers.Default) {
        mapJobs.awaitAll()
    }
}

/**
 * Performs an asynchronous transformation on each element of the iterable collection,
 * returning a list of the results, excluding null values.
 *
 * This function is designed to be used with suspend functions, allowing for non-blocking
 * execution of the transformation. The transformation is performed concurrently on each
 * element using coroutines, with the [Dispatchers.IO] dispatcher.
 *
 * @param transform A suspend function that takes an element of the iterable collection
 * and returns a transformed result, which may be null.
 *
 * @return A list of the non-null results of applying the [transform] function to each element
 * of the iterable collection. The order of the results is the same as the order of the
 * elements in the original collection.
 *
 * @throws CancellationException If the coroutine is cancelled.
 * @throws InterruptedException If the coroutine is interrupted.
 * @throws TimeoutException If the coroutine times out.
 * @throws Exception If an exception is thrown during the transformation.
 */
suspend inline fun <V, R : Any> Iterable<V>.mapNotNullAsync(crossinline transform: suspend (V) -> R?): List<R> = coroutineScope {
    val mapJobs : MutableList<Deferred<R?>> = mutableListOf()

    forEachIndexed { index, entry ->
        mapJobs.add(
            index,
            async(Dispatchers.IO) { transform(entry) }
        )
    }

    withContext(Dispatchers.Default) {
        mapJobs.awaitAll().filterNotNull()
    }
}

/**
 * Performs an asynchronous transformation on each element of the iterable collection,
 * returning a list of the non-null results, including the index of each element.
 *
 * This function is designed to be used with suspend functions, allowing for non-blocking
 * execution of the transformation. The transformation is performed concurrently on each
 * element using coroutines, with the [Dispatchers.IO] dispatcher.
 *
 * @param transform A suspend function that takes the index and an element of the iterable collection
 * and returns a transformed result, which may be null.
 *
 * @return A list of the non-null results of applying the [transform] function to each element
 * of the iterable collection. The order of the results is the same as the order of the
 * elements in the original collection.
 *
 * @throws CancellationException If the coroutine is cancelled.
 * @throws InterruptedException If the coroutine is interrupted.
 * @throws TimeoutException If the coroutine times out.
 * @throws Exception If an exception is thrown during the transformation.
 */
suspend inline fun <V, R : Any> Iterable<V>.mapNotNullAsyncIndexed(crossinline transform: suspend (Int, V) -> R?): List<R> = coroutineScope {
    val mapJobs : MutableList<Deferred<R?>> = mutableListOf()

    forEachIndexed { index, entry ->
        mapJobs.add(
            index,
            async(Dispatchers.IO) { transform(index, entry) }
        )
    }

    withContext(Dispatchers.Default) {
        mapJobs.awaitAll().filterNotNull()
    }
}

/**
 * Performs an asynchronous transformation on each entry of the map, returning a list of the results.
 *
 * This function is designed to be used with suspend functions, allowing for non-blocking
 * execution of the transformation. The transformation is performed concurrently on each
 * entry using coroutines, with the [Dispatchers.IO] dispatcher.
 *
 * @param transform A suspend function that takes a key and a value from the map
 * and returns a transformed result.
 *
 * @return A list of the results of applying the [transform] function to each entry
 * of the map. The order of the results is the same as the order of the entries in the original map.
 *
 * @throws CancellationException If the coroutine is cancelled.
 * @throws InterruptedException If the coroutine is interrupted.
 * @throws TimeoutException If the coroutine times out.
 * @throws Exception If an exception is thrown during the transformation.
 */
suspend fun <K, V, R> Map<K, V>.mapAsync(transform: suspend (K, V) -> R): List<R> = coroutineScope {
    val mapJobs : MutableList<Deferred<R>> = mutableListOf()

    onEachIndexed{ index, entry ->
        mapJobs.add(
            index,
            async(Dispatchers.IO) { transform(entry.key, entry.value) }
        )
    }

    withContext(Dispatchers.Default) {
        mapJobs.awaitAll()
    }
}

/**
 * Performs an asynchronous transformation on each entry of the map, returning a list of the non-null results.
 *
 * This function is designed to be used with suspend functions, allowing for non-blocking
 * execution of the transformation. The transformation is performed concurrently on each
 * entry using coroutines, with the [Dispatchers.IO] dispatcher.
 *
 * @param transform A suspend function that takes a key and a value from the map
 * and returns a transformed result, which may be null.
 *
 * @return A list of the non-null results of applying the [transform] function to each entry
 * of the map. The order of the results is the same as the order of the entries in the original map.
 *
 * @throws CancellationException If the coroutine is cancelled.
 * @throws InterruptedException If the coroutine is interrupted.
 * @throws TimeoutException If the coroutine times out.
 * @throws Exception If an exception is thrown during the transformation.
 */
suspend inline fun <K, V, R : Any> Map<K, V>.mapNotNullAsync(crossinline transform: suspend (K, V) -> R?): List<R> = coroutineScope {
    val mapJobs : MutableList<Deferred<R?>> = mutableListOf()

    onEachIndexed{ index, entry ->
        mapJobs.add(
            index,
            async(Dispatchers.IO) { transform(entry.key, entry.value) }
        )
    }

    withContext(Dispatchers.Default) {
        mapJobs.awaitAll().filterNotNull()
    }
}