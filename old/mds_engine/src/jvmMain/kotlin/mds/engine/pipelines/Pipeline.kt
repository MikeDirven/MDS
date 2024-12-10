package mds.engine.pipelines

sealed interface Pipeline {
    interface SubPipeline : Pipeline {
        val applicationPipeline: ApplicationPipeline
    }
}