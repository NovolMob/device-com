package repositories

import arrow.core.Either
import client.CustomHttpClient
import org.koin.core.component.KoinComponent
import ru.novolmob.backend.ktorrouting.worker.Login
import ru.novolmob.backend.ktorrouting.worker.Logout
import ru.novolmob.backend.ktorrouting.worker.Worker
import ru.novolmob.backend.ktorrouting.worker.Workers
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.models.ids.WorkerId

interface IWorkerRepository: IRepository {
    suspend fun logout(): Either<AbstractBackendException, Unit>
    suspend fun login(email: Email, password: Password): Either<AbstractBackendException, TokenModel>
    suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<AbstractBackendException, TokenModel>
    suspend fun post(createModel: WorkerCreateModel): Either<AbstractBackendException, WorkerModel>
    suspend fun put(id: WorkerId, updateModel: WorkerUpdateModel): Either<AbstractBackendException, WorkerModel>
    suspend fun post(id: WorkerId, createModel: WorkerCreateModel): Either<AbstractBackendException, WorkerModel>
    suspend fun get(): Either<AbstractBackendException, WorkerModel>
    suspend fun get(id: WorkerId): Either<AbstractBackendException, WorkerModel>
    suspend fun delete(id: WorkerId): Either<AbstractBackendException, Boolean>
    suspend fun getAll(workers: Workers = Workers()): Either<AbstractBackendException, Page<WorkerModel>>
}

class WorkerRepositoryImpl(
    private val client: CustomHttpClient
): KoinComponent, IWorkerRepository {

    override suspend fun logout(): Either<AbstractBackendException, Unit> =
        client.post(resource = Logout())

    override suspend fun login(
        email: Email,
        password: Password
    ): Either<AbstractBackendException, TokenModel> =
        client.post(
            resource = Login(),
            body = LoginModel(email = email, password = password)
        )

    override suspend fun login(
        phoneNumber: PhoneNumber,
        password: Password
    ): Either<AbstractBackendException, TokenModel> =
        client.post(
            resource = Login(),
            body = LoginModel(phoneNumber = phoneNumber, password = password)
        )

    override suspend fun get(): Either<AbstractBackendException, WorkerModel> =
        client.get(resource = Worker())

    override suspend fun delete(id: WorkerId): Either<AbstractBackendException, Boolean> =
        client.delete(resource = Workers.Id(id))

    override suspend fun get(id: WorkerId): Either<AbstractBackendException, WorkerModel> =
        client.delete(resource = Workers.Id(id))

    override suspend fun getAll(workers: Workers): Either<AbstractBackendException, Page<WorkerModel>> =
        client.get(resource = workers)

    override suspend fun post(createModel: WorkerCreateModel): Either<AbstractBackendException, WorkerModel> =
        client.post(resource = Workers(), body = createModel)

    override suspend fun post(id: WorkerId, createModel: WorkerCreateModel): Either<AbstractBackendException, WorkerModel> =
        client.post(resource = Workers.Id(id), body = createModel)

    override suspend fun put(id: WorkerId, updateModel: WorkerUpdateModel): Either<AbstractBackendException, WorkerModel> =
        client.put(resource = Workers.Id(id), body = updateModel)

}