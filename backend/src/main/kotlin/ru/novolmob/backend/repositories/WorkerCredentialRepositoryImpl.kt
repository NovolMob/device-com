package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.credentialByIdNotFoundException
import ru.novolmob.backend.exceptions.workerByIdNotFound
import ru.novolmob.backend.exceptions.workerCredentialByUserIdNotFoundException
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IWorkerCredentialRepository
import ru.novolmob.exposeddatabase.entities.Worker
import ru.novolmob.exposeddatabase.entities.WorkerCredential
import ru.novolmob.core.models.UpdateDate
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.exposeddatabase.tables.credentials.WorkerCredentials

class WorkerCredentialRepositoryImpl(
    val mapper: Mapper<WorkerCredential, WorkerCredentialModel>,
    val resultRowMapper: Mapper<ResultRow, WorkerCredentialModel>
): IWorkerCredentialRepository {
    override suspend fun getByWorkerId(workerId: WorkerId): Either<BackendException, WorkerCredentialModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            WorkerCredential.find { WorkerCredentials.worker eq workerId }
                .limit(1).firstOrNull()?.let(mapper::invoke) ?: workerCredentialByUserIdNotFoundException(workerId).left()
        }

    override suspend fun get(id: CredentialId): Either<BackendException, WorkerCredentialModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            WorkerCredential.findById(id)?.let(mapper::invoke) ?: credentialByIdNotFoundException(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<WorkerCredentialModel>> =
        RepositoryUtil.generalGatAll(WorkerCredentials, pagination, resultRowMapper)

    override suspend fun post(createModel: WorkerCredentialCreateModel): Either<BackendException, WorkerCredentialModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val worker = Worker.findById(createModel.workerId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.workerId).left()
            WorkerCredential.new {
                this.worker = worker
                this.phoneNumber = createModel.phoneNumber
                this.email = createModel.email
                this.password = createModel.password
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: CredentialId,
        createModel: WorkerCredentialCreateModel
    ): Either<BackendException, WorkerCredentialModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val worker = Worker.findById(createModel.workerId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.workerId).left()
            WorkerCredential.findById(id)?.apply {
                this.worker = worker
                this.phoneNumber = createModel.phoneNumber
                this.email = createModel.email
                this.password = createModel.password
            }?.let(mapper::invoke) ?: credentialByIdNotFoundException(id).left()
        }

    override suspend fun put(
        id: CredentialId,
        updateModel: WorkerCredentialUpdateModel
    ): Either<BackendException, WorkerCredentialModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val worker = updateModel.workerId?.let {
                Worker.findById(it) ?: return@newSuspendedTransaction workerByIdNotFound(it).left()
            }
            WorkerCredential.findById(id)?.apply {
                worker?.let { this.worker = it }
                updateModel.phoneNumber?.let { this.phoneNumber }
                updateModel.email?.let { this.email }
                updateModel.password?.let { this.password }
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: credentialByIdNotFoundException(id).left()
        }

    override suspend fun delete(id: CredentialId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            WorkerCredential.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}