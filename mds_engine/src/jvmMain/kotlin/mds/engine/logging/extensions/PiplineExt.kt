package mds.engine.logging.extensions

import mds.engine.pipelines.Pipeline

fun Pipeline.SubPipeline.info(message: String) = applicationPipeline.application.logger.info(message)
fun Pipeline.SubPipeline.warn(message: String) = applicationPipeline.application.logger.warn(message)
fun Pipeline.SubPipeline.error(message: String) = applicationPipeline.application.logger.error(message)

fun Pipeline.SubPipeline.trace(message: String) = applicationPipeline.application.logger.trace(message)
fun Pipeline.SubPipeline.debug(message: String) = applicationPipeline.application.logger.debug(message)