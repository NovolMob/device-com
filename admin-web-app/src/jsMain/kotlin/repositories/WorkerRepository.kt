package repositories

import arrow.core.Either
import io.ktor.client.*
import org.koin.core.component.KoinComponent
import ru.novolmob.backend.ktorrouting.worker.Workers
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import utils.KtorUtil.get
import utils.KtorUtil.post

interface IWorkerRepository: IRepository {
    suspend fun logout(): Either<AbstractBackendException, Unit>
    suspend fun login(email: Email, password: Password): Either<AbstractBackendException, TokenModel>
    suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<AbstractBackendException, TokenModel>
    suspend fun get(): Either<AbstractBackendException, WorkerModel>
}

class WorkerRepositoryImpl(
    private val client: HttpClient
): KoinComponent, IWorkerRepository {

    override suspend fun logout(): Either<AbstractBackendException, Unit> =
        client.post(resource = Workers.Logout())

    override suspend fun login(
        email: Email,
        password: Password
    ): Either<AbstractBackendException, TokenModel> =
        client.post(
            resource = Workers.Login(),
            body = LoginModel(email = email, password = password)
        )

    override suspend fun login(
        phoneNumber: PhoneNumber,
        password: Password
    ): Either<AbstractBackendException, TokenModel> =
        client.post(
            resource = Workers.Login(),
            body = LoginModel(phoneNumber = phoneNumber, password = password)
        )

    override suspend fun get(): Either<AbstractBackendException, WorkerModel> =
        client.get(resource = Workers.Worker())

}