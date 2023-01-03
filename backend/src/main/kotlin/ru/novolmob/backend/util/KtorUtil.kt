package ru.novolmob.backend.util

import arrow.core.Either
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.backendapi.models.ResponseModel

object KtorUtil {
    fun BackendExceptionCode.toHttpStatusCode(): HttpStatusCode = HttpStatusCode(httpCode, description)

    suspend inline fun <reified T> ApplicationCall.respondData(data: T, code: HttpStatusCode = HttpStatusCode.OK) =
        respond(status = code, message = ResponseModel(data = data))
    suspend fun ApplicationCall.respondException(exception: BackendException) =
        respond(status = exception.code.toHttpStatusCode(), message = ResponseModel<Unit>(exception = exception))
    suspend fun ApplicationCall.respondException(code: BackendExceptionCode, message: String) =
        respondException(BackendException(code = code, message = message))
    suspend inline fun <reified T> ApplicationCall.respond(either: Either<BackendException, T>, successCode: HttpStatusCode = HttpStatusCode.OK) =
        either.fold(
            ifLeft = { respondException(it) },
            ifRight = { respondData(it, successCode) }
        )
}