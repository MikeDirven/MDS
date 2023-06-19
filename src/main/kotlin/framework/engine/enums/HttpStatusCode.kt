package framework.engine.enums

enum class HttpStatusCode(val code: Int) {
    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
}