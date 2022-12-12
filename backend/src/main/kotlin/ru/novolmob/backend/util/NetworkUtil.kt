package ru.novolmob.backend.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.backendapi.models.ResponseModel

object NetworkUtil {
    fun BackendExceptionCode.toHttpStatusCode(): HttpStatusCode = HttpStatusCode(httpCode, description)

    suspend fun <T> ApplicationCall.respondData(data: T, code: HttpStatusCode = HttpStatusCode.OK) =
        respond(status = code, message = ResponseModel(data = data))
    suspend fun ApplicationCall.respondException(exception: BackendException) =
        respond(status = exception.code.toHttpStatusCode(), message = ResponseModel<Unit>(exception = exception))
    suspend fun ApplicationCall.respondException(code: BackendExceptionCode, message: String) =
        respondException(BackendException(code = code, message = message))
}