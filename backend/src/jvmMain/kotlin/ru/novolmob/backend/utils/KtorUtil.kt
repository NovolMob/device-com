package ru.novolmob.backend.utils

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.backendapi.models.ResponseModel

object KtorUtil {
    fun BackendExceptionCode.toHttpStatusCode(): HttpStatusCode = HttpStatusCode(httpCode, description)

    suspend inline fun <reified T> ApplicationCall.respondData(data: T, code: HttpStatusCode = HttpStatusCode.OK) =
        respond(status = code, message = ResponseModel(data = data))
    suspend fun ApplicationCall.respondException(exception: AbstractBackendException) =
        respond(status = exception.code.toHttpStatusCode(), message = ResponseModel<Unit>(exception = exception))
    suspend fun ApplicationCall.respondException(code: BackendExceptionCode, message: String) =
        respondException(AbstractBackendException.BackendException(code = code, message = message))
    suspend inline fun <reified T> ApplicationCall.respondException(code: BackendExceptionCode, message: String, payload: T? = null) =
        respondException(AbstractBackendException.PayloadBackendException(code = code, message = message).payload(payload))
    suspend inline fun <reified T> ApplicationCall.respond(either: Either<AbstractBackendException, T>, successCode: HttpStatusCode = HttpStatusCode.OK) =
        either.fold(
            ifLeft = { respondException(it) },
            ifRight = { respondData(it, successCode) }
        )
}