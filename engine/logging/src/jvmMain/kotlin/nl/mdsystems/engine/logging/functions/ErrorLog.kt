package nl.mdsystems.engine.logging.functions

import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.constants.LoggingTags

/**
 * Logs an error message with the given [message] and the current thread information.
 *
 * @param message The error message to be logged.
 */
fun MdsEngineLogging.error(message: String) {
    configuration.logger.error(
        LoggingTags.Colors.RED + LoggingTags.thread + message + LoggingTags.Colors.RESET
    )
}

/**
 * Logs an error message with the given [message] function, which is evaluated lazily, and the current thread information.
 *
 * This function is useful when the error message is expensive to compute and should not be created unless necessary.
 *
 * @param message A function that returns the error message to be logged. This function is evaluated lazily, meaning it will
 *                only be called if an error log is actually triggered.
 */
fun MdsEngineLogging.error(message: () -> String) {
    configuration.logger.error(
        LoggingTags.Colors.RED + LoggingTags.thread + message() + LoggingTags.Colors.RESET
    )
}


/**
 * Logs an error message with the given [Throwable] and the current thread information.
 *
 * This function is useful for logging exceptions and their stack traces. It retrieves the stack trace from the given
 * [Throwable] and formats it into a string, which is then logged along with the current thread information.
 *
 * @param cause The [Throwable] whose stack trace will be logged. This can be any exception or error that occurred during
 *              the execution of the code.
 *
 * @return Unit. This function does not return any value.
 */
fun MdsEngineLogging.error(cause: Throwable) {
    configuration.logger.error(
        LoggingTags.Colors.RED + LoggingTags.thread + cause.message.plus("\n") + cause.stackTrace.joinToString("\n") + LoggingTags.Colors.RESET
    )
}