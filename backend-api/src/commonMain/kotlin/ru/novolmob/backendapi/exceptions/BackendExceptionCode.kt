package ru.novolmob.backendapi.exceptions

enum class BackendExceptionCode(val httpCode: Int, val description: String) {
    UNKNOWN(500, ""),
    BAD_REQUEST(400, ""),
    NOT_ENOUGH_DEVICES(400, ""),
    FORBIDDEN(403, ""),
    NOT_AUTHORIZED(403, ""),
    DONT_HAVE_RIGHT(403, ""),
    DETAIL_IS_EXISTS(400, ""),
    NOT_FOUND(404, "");
}