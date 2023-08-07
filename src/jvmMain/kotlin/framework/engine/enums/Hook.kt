package framework.engine.enums

enum class Hook {
    BEFORE_STARTING,
    SERVER_STARTED,
    REQUEST_RECEIVED,
    BEFORE_BODY_READ,
    BODY_RECEIVED,
    RESPONSE_READY,
    RESPONSE_SEND,
    SHUTDOWN
}