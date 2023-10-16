package mds.engine.interfaces

interface MdsEngineServerConfig {
    /**
     * Port to listen on. Defaults to 8080
     */
    var port: Int

    /**
     * The host to listen on
     */
    var host: String

}