package ru.novolmob.user_mobile_app.utils

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.backendapi.models.ResponseModel

object KtorUtil {
    suspend inline fun <reified E> HttpResponse.response(): Either<BackendException, E> =
        kotlin.runCatching {
            body<ResponseModel<E>>().let { model ->
                model.exception?.left() ?: model.data!!.right()
            }
        }.getOrElse {
            BackendException(
                code = BackendExceptionCode.NOT_FOUND,
                message = "Body is empty!"
            ).left()
        }

    suspend inline fun <reified Resource: Any, reified Body, reified Response> HttpClient.post(resource: Resource, body: Body): Either<BackendException, Response> =
        post(
            resource = resource,
            builder = {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        ).response()

    suspend inline fun <reified Resource: Any, reified Response> HttpClient.post(resource: Resource): Either<BackendException, Response> =
        post(
            resource = resource,
            builder = { }
        ).response()

    suspend inline fun <reified Resource: Any, reified Body, reified Response> HttpClient.put(resource: Resource, body: Body): Either<BackendException, Response> =
        put(
            resource = resource,
            builder = {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        ).response()

    suspend inline fun <reified Resource: Any, reified Response> HttpClient.put(resource: Resource): Either<BackendException, Response> =
        put(
            resource = resource,
            builder = { }
        ).response()

    suspend inline fun <reified Resource: Any, reified Response> HttpClient.get(resource: Resource): Either<BackendException, Response> =
        get(resource = resource, builder = {}).response()

    suspend inline fun <reified Resource: Any, reified Response> HttpClient.delete(resource: Resource): Either<BackendException, Response> =
        delete(resource = resource, builder = {}).response()
}