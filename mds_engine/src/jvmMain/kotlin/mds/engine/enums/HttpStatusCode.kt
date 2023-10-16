package mds.engine.enums

enum class HttpStatusCode(val code: Int) {
    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500)
}