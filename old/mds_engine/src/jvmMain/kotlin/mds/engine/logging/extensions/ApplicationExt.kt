package mds.engine.logging.extensions

import mds.engine.handlers.Application
import mds.engine.plugins.extensions.plugin

fun Application.info(message: String) = logger.info(message)
fun Application.warn(message: String) = logger.warn(message)
fun Application.error(message: String) = logger.error(message)

fun Application.trace(message: String) = logger.trace(message)
fun Application.debug(message: String) = logger.debug(message)