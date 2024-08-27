package nl.mdsystems.engine.routing.types

import nl.mdsystems.engine.pipelines.ResponsePipeline

typealias RouteHandler = ResponsePipeline.() -> Unit