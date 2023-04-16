package client

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
import storages.TokenStorage

class CustomHttpClient(
    val ktorClient: HttpClient,
    val tokenStorage: TokenStorage
) {

    suspend inline fun <reified Resource: Any, reified Body, reified Response> post(resource: Resource, body: Body): Either<AbstractBackendException, Response> =
        ktorClient.post(
            resource = resource,
            builder = {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        ).response()

    suspend inline fun <reified Resource: Any, reified Response> post(resource: Resource): Either<AbstractBackendException, Response> =
        ktorClient.post(
            resource = resource,
            builder = { }
        ).response()

    suspend inline fun <reified Resource: Any, reified Body, reified Response> put(resource: Resource, body: Body): Either<AbstractBackendException, Response> =
        ktorClient.put(
            resource = resource,
            builder = {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        ).response()

    suspend inline fun <reified Resource: Any, reified Response> put(resource: Resource): Either<AbstractBackendException, Response> =
        ktorClient.put(
            resource = resource,
            builder = { }
        ).response()

    suspend inline fun <reified Resource: Any, reified Response> get(resource: Resource): Either<AbstractBackendException, Response> =
        ktorClient.get(resource = resource, builder = {}).response()

    suspend inline fun <reified Resource: Any, reified Response> delete(resource: Resource): Either<AbstractBackendException, Response> =
        ktorClient.delete(resource = resource, builder = {}).response()

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
        }.also {
            it.fold(
                ifLeft = {
                    if (it.code == BackendExceptionCode.NOT_AUTHORIZED)
                        tokenStorage.set(null)
                },
                ifRight = {

                }
            )
        }

}