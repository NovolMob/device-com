package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.badCredentialsException
import ru.novolmob.backendapi.exceptions.pointByIdNotFound
import ru.novolmob.backendapi.exceptions.workerByIdNotFound
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IWorkerCredentialRepository
import ru.novolmob.backendapi.repositories.IWorkerRepository
import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.exposeddatabase.entities.Point
import ru.novolmob.exposeddatabase.entities.Worker
import ru.novolmob.exposeddatabase.entities.credentials.WorkerCredential
import ru.novolmob.exposeddatabase.tables.Workers
import ru.novolmob.exposeddatabase.tables.credentials.WorkerCredentials

class WorkerRepositoryImpl(
    val mapper: Mapper<Worker, WorkerModel>,
    val resultRowInfoMapper: Mapper<ResultRow, WorkerInfoModel>,
    val workerCredentialRepository: IWorkerCredentialRepository
): IWorkerRepository {

    override suspend fun getAllByPointId(pointId: PointId): Either<AbstractBackendException, List<WorkerModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            Worker.find { Workers.point eq pointId }.parTraverseEither { mapper(it) }
        }

    override suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<AbstractBackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            WorkerCredential.find { (WorkerCredentials.phoneNumber eq phoneNumber) and (WorkerCredentials.password eq password) }
                .limit(1).firstOrNull()?.let {
                    mapper(it.parent)
                } ?: badCredentialsException().left()
        }

    override suspend fun login(email: Email, password: Password): Either<AbstractBackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            WorkerCredential.find { (WorkerCredentials.email eq email) and (WorkerCredentials.password eq password) }
                .limit(1).firstOrNull()?.let {
                    mapper(it.parent)
                } ?: badCredentialsException().left()
        }

    override suspend fun get(id: WorkerId): Either<AbstractBackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Worker.findById(id)?.let(mapper::invoke) ?: workerByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<WorkerModel>> =
        RepositoryUtil.generalGetAll(Workers, pagination, resultRowInfoMapper).flatMap { page ->
            page.list.parTraverseEither { infoModel ->
                workerCredentialRepository.getBy(infoModel.id).flatMap { credential ->
                    WorkerModel(
                        id = infoModel.id,
                        pointId = infoModel.pointId,
                        firstname = infoModel.firstname,
                        lastname = infoModel.lastname,
                        patronymic = infoModel.patronymic,
                        language = infoModel.language,
                        phoneNumber = credential.phoneNumber,
                        email = credential.email
                    ).right()
                }
            }.flatMap {
                Page(page = page.page, size = page.size, list = it).right()
            }
        }

    override suspend fun post(createModel: WorkerCreateModel): Either<AbstractBackendException, WorkerModel> =
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
                    WorkerCredentialModel(
                        workerId = it.id.value,
                        phoneNumber = createModel.phoneNumber,
                        email = createModel.email,
                        password = createModel.password
                    )
                )
            }.let(mapper::invoke)
        }

    override suspend fun post(id: WorkerId, createModel: WorkerCreateModel): Either<AbstractBackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = createModel.pointId?.let {
                Point.findById(it) ?: return@newSuspendedTransaction pointByIdNotFound(it).left()
            }
            val worker = Worker.findById(id) ?: return@newSuspendedTransaction workerByIdNotFound(id).left()
            workerCredentialRepository.put(id, createModel).flatMap {
                worker.apply {
                    this.point = point
                    this.firstname = createModel.firstname
                    this.lastname = createModel.lastname
                    this.patronymic = createModel.patronymic
                    this.language = createModel.language
                    this.updateDate = UpdateTime.now()
                }.let(mapper::invoke)
            }
        }

    override suspend fun put(id: WorkerId, updateModel: WorkerUpdateModel): Either<AbstractBackendException, WorkerModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = updateModel.pointId?.let {
                Point.findById(it) ?: return@newSuspendedTransaction pointByIdNotFound(it).left()
            }
            val worker = Worker.findById(id) ?: return@newSuspendedTransaction workerByIdNotFound(id).left()
            workerCredentialRepository.put(id, updateModel).flatMap {
                worker.apply {
                    point?.let { this.point = it }
                    updateModel.firstname?.let { this.firstname = it }
                    updateModel.lastname?.let { this.lastname = it }
                    updateModel.patronymic?.let { this.patronymic = it }
                    updateModel.language?.let { this.language = it }
                    this.updateDate = UpdateTime.now()
                }.let(mapper::invoke)
            }
        }

    override suspend fun delete(id: WorkerId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Worker.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

}