package ru.novolmob.backendapi.exceptions

enum class BackendExceptionCode(val httpCode: Int, val description: String) {
    UNKNOWN(500, ""),
    BAD_REQUEST(400, ""),
    FORBIDDEN(403, ""),
    NOT_FOUND(404, "");
}