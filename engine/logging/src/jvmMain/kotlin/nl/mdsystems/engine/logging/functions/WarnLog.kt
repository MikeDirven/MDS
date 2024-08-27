package nl.mdsystems.engine.logging.functions

import nl.mdsystems.engine.logging.MdsEngineLogging
import nl.mdsystems.engine.logging.constants.LoggingTags

/**
 * Logs a warning message with the given [message] and the current thread information.
 *
 * @param message The warning message to be logged. It can be a string or a lambda function that returns a string.
 *
 * @return Nothing.
 */
fun MdsEngineLogging.warn(message: String) {
    configuration.logger.warn(
        LoggingTags.Colors.YELLOW + LoggingTags.thread + message + LoggingTags.Colors.RESET
    )
}

/**
 * Logs a warning message with the given lambda function [message] and the current thread information.
 *
 * This function is designed to log warning messages in the MdsEngineLogging system. It takes a lambda function
 * as a parameter, which allows for dynamic message generation. The function appends the current thread information
 * to the message before logging it.
 *
 * @param message A lambda function that returns a string. This string represents the warning message to be logged.
 *
 * @return Nothing. This function does not return any value.
 */
fun MdsEngineLogging.warn(message: () -> String) {
    configuration.logger.warn(
        LoggingTags.Colors.YELLOW + LoggingTags.thread + message() + LoggingTags.Colors.RESET
    )
}