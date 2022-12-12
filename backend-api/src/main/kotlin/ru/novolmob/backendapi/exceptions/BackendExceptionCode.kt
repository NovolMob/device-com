package ru.novolmob.backendapi.exceptions

enum class BackendExceptionCode(val httpCode: Int, val description: String) {
    UNKNOWN(500, "")
}