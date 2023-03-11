package utils

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.backendapi.models.ResponseModel

object KtorUtil {
    suspend inline fun <reified E> HttpResponse.response(): Either<AbstractBackendException, E> =
        kotlin.runCatching {
            body<ResponseModel<E>>().let { model ->
                model.exception?.left() ?: model.data!!.right()
            }
        }.getOrElse {
            it.printStackTrace()
            AbstractBackendException.BackendException(
                code = BackendExceptionCode.NOT_FOUND,
                message = "Body is empty!"
            ).left()
        }

    suspend inline fun <reified Resource: Any, reified Body, reified Response> HttpClient.post(resource: Resource, body: Body): Either<AbstractBackendException, Response> =
        post(
            resource = resource,
            builder = {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        ).response()

    suspend inline fun <reified Resource: Any, reified Response> HttpClient.post(resource: Resource): Either<AbstractBackendException, Response> =
        post(
            resource = resource,
            builder = { }
        ).response()

    suspend inline fun <reified Resource: Any, reified Body, reified Response> HttpClient.put(resource: Resource, body: Body): Either<AbstractBackendException, Response> =
        put(
            resource = resource,
            builder = {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        ).response()

    suspend inline fun <reified Resource: Any, reified Response> HttpClient.put(resource: Resource): Either<AbstractBackendException, Response> =
        put(
            resource = resource,
            builder = { }
        ).response()

    suspend inline fun <reified Resource: Any, reified Response> HttpClient.get(resource: Resource): Either<AbstractBackendException, Response> =
        get(resource = resource, builder = {}).response()

    suspend inline fun <reified Resource: Any, reified Response> HttpClient.delete(resource: Resource): Either<AbstractBackendException, Response> =
        delete(resource = resource, builder = {}).response()
}