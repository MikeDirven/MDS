package framework.engine.interfaces

import framework.engine.MdsEngine

interface MdsEngineConfigBuilder {
    /**
     * Port to listen on. Defaults to 8080
     */
    var port: Int

    /**
     * The host to listen on
     */
    var host: String

    /**
     * The maximum amount of thread pools to use for incoming requests
     */
    var maxPools: Int

    /**
     * The maximum amount of thread per pool to use for incoming requests
     */
    var maxPoolThreads: Int

    /**
     * The entry point to activate while building te server
     */
    var entryPoint: (engine: MdsEngine) -> Unit
}