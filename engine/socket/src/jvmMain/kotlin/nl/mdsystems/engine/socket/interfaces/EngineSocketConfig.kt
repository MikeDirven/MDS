package nl.mdsystems.engine.socket.interfaces

/**
 * Interface defining the configuration parameters for an application socket.
 *
 * This interface provides a contract for classes that need to configure a socket for an application.
 * It includes properties for the host, port, and backlog, which are commonly used in socket programming.
 *
 * @property host The host or IP address to which the socket will connect.
 * @property port The port number on which the socket will listen or connect.
 * @property backlog The maximum number of pending connections that can be queued for acceptance.
 */
interface EngineSocketConfig {
    var host: String
    var port: Int
    var backlog: Int
}