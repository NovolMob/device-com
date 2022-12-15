package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.badCredentialsException
import ru.novolmob.backend.exceptions.pointByIdNotFound
import ru.novolmob.backend.exceptions.workerByIdNotFound
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IWorkerCredentialRepository
import ru.novolmob.backendapi.repositories.IWorkerRepository
import ru.novolmob.database.entities.Point
import ru.novolmob.database.entities.Worker
import ru.novolmob.database.entities.WorkerCredential
import ru.novolmob.database.models.Email
import ru.novolmob.database.models.Password
import ru.novolmob.database.models.PhoneNumber
import ru.novolmob.database.models.UpdateDate
import ru.novolmob.database.models.ids.PointId
import ru.novolmob.database.models.ids.WorkerId
import ru.novolmob.database.tables.Workers
import ru.novolmob.database.tables.credentials.WorkerCredentials

class WorkerRepositoryImpl(
    val mapper: Mapper<Worker, WorkerModel>,
    val resultRowMapper: Mapper<ResultRow, WorkerModel>,
    val workerCredentialRepository: IWorkerCredentialRepository
): IWorkerRepository {
    override suspend fun getAllByPointId(pointId: PointId): Either<BackendException, List<WorkerModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            Worker.find { Workers.point eq pointId }.parTraverseEither { mapper(it) }
        }

    override suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<BackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            WorkerCredential.find { (WorkerCredentials.phoneNumber eq phoneNumber) and (WorkerCredentials.password eq password) }
                .limit(1).firstOrNull()?.let {
                    mapper(it.worker)
                } ?: badCredentialsException().left()
        }

    override suspend fun login(email: Email, password: Password): Either<BackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            WorkerCredential.find { (WorkerCredentials.email eq email) and (WorkerCredentials.password eq password) }
                .limit(1).firstOrNull()?.let {
                    mapper(it.worker)
                } ?: badCredentialsException().left()
        }

    override suspend fun get(id: WorkerId): Either<BackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Worker.findById(id)?.let(mapper::invoke) ?: workerByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<WorkerModel>> =
        RepositoryUtil.generalGatAll(Workers, pagination, resultRowMapper)

    override suspend fun post(createModel: WorkerCreateModel): Either<BackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = createModel.pointId?.let {
                Point.findById(it) ?: return@newSuspendedTransaction pointByIdNotFound(it).left()
            }
            Worker.new {
                this.point = point
                this.firstname = createModel.firstname
                this.lastname = createModel.lastname
                this.patronymic = createModel.patronymic
                this.language = createModel.language
            }.also {
                workerCredentialRepository.post(
                    WorkerCredentialCreateModel(
                        workerId = it.id.value,
                        phoneNumber = createModel.phoneNumber,
                        email = createModel.email,
                        password = createModel.password
                    )
                )
            }.let(mapper::invoke)
        }

    override suspend fun post(id: WorkerId, createModel: WorkerCreateModel): Either<BackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = createModel.pointId?.let {
                Point.findById(it) ?: return@newSuspendedTransaction pointByIdNotFound(it).left()
            }
            workerCredentialRepository.getByWorkerId(id).flatMap {
                workerCredentialRepository.put(
                    id = it.id,
                    updateModel = WorkerCredentialUpdateModel(
                        workerId = null,
                        phoneNumber = createModel.phoneNumber,
                        email = createModel.email,
                        password = createModel.password
                    )
                ).flatMap {
                    Worker.findById(id)?.apply {
                        this.point = point
                        this.firstname = createModel.firstname
                        this.lastname = createModel.lastname
                        this.patronymic = createModel.patronymic
                        this.language = createModel.language
                        this.updateDate = UpdateDate.now()
                    }?.let(mapper::invoke) ?: workerByIdNotFound(id).left()
                }
            }
        }

    override suspend fun put(id: WorkerId, updateModel: WorkerUpdateModel): Either<BackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = updateModel.pointId?.let {
                Point.findById(it) ?: return@newSuspendedTransaction pointByIdNotFound(it).left()
            }
            workerCredentialRepository.getByWorkerId(id).flatMap {
                workerCredentialRepository.put(
                    id = it.id,
                    updateModel = WorkerCredentialUpdateModel(
                        workerId = null,
                        phoneNumber = updateModel.phoneNumber,
                        email = updateModel.email,
                        password = updateModel.password
                    )
                ).flatMap {
                    Worker.findById(id)?.apply {
                        point?.let { this.point = it }
                        updateModel.firstname?.let { this.firstname = it }
                        updateModel.lastname?.let { this.lastname = it }
                        updateModel.patronymic?.let { this.patronymic = it }
                        updateModel.language?.let { this.language = it }
                        this.updateDate = UpdateDate.now()
                    }?.let(mapper::invoke) ?: workerByIdNotFound(id).left()
                }
            }
        }

    override suspend fun delete(id: WorkerId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Worker.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

}