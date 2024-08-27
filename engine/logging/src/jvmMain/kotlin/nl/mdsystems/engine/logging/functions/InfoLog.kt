package nl.mdsystems.engine.logging.functions

import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.constants.LoggingTags

/**
 * Logs an informational message to the configured logger.
 *
 * This function takes a [message] as a string and appends the current thread information to it.
 * The formatted message is then logged using the configured logger at the INFO level.
 *
 * @param message The informational message to be logged.
 */
fun MdsEngineLogging.info(message: String) {
    configuration.logger.info(
        LoggingTags.thread + message
    )
}

/**
 * Logs an informational message to the configured logger, with the message being a lambda function.
 *
 * This function takes a lambda function [message] that returns a string. The lambda function is invoked
 * to obtain the actual message to be logged. The current thread information is appended to the message.
 * The formatted message is then logged using the configured logger at the INFO level.
 *
 * @param message A lambda function that returns the informational message to be logged.
 *
 * @return Unit (This function does not return any value).
 */
fun MdsEngineLogging.info(message: () -> String) {
    configuration.logger.info(
        LoggingTags.thread + message()
    )
}