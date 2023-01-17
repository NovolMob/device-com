package ru.novolmob.exposedbackendapi.repositories.credentials

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.jetbrains.exposed.sql.or
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.emailOrPhoneNumberNotUnique
import ru.novolmob.backendapi.exceptions.workerByIdNotFound
import ru.novolmob.backendapi.exceptions.workerCredentialByWorkerIdNotFoundException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.WorkerCredentialModel
import ru.novolmob.backendapi.models.WorkerCredentialUpdate
import ru.novolmob.backendapi.repositories.IWorkerCredentialRepository
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.exposeddatabase.entities.Worker
import ru.novolmob.exposeddatabase.entities.credentials.WorkerCredential
import ru.novolmob.exposeddatabase.tables.credentials.WorkerCredentials

class WorkerCredentialRepositoryImpl(
    mapper: Mapper<WorkerCredential, WorkerCredentialModel>,
): IWorkerCredentialRepository,
    AbstractCredentialRepository<WorkerCredential.Companion, WorkerCredential, Worker, WorkerId, WorkerCredentialModel, WorkerCredentialUpdate>(
        WorkerCredential.Companion, mapper, ::workerCredentialByWorkerIdNotFoundException
    ) {

    override suspend fun WorkerCredential.Companion.new(createModel: WorkerCredentialModel): Either<AbstractBackendException, WorkerCredential> {
        if (!WorkerCredential.find { (WorkerCredentials.email eq createModel.email) or (WorkerCredentials.phoneNumber eq createModel.phoneNumber) }.empty())
            return emailOrPhoneNumberNotUnique().left()
        val worker = Worker.findById(createModel.workerId) ?: return workerByIdNotFound(createModel.workerId).left()
        return new {
            this.parent = worker
            this.phoneNumber = createModel.phoneNumber
            this.email = createModel.email
            this.password = createModel.password
        }.right()
    }

    override suspend fun WorkerCredential.apply(updateModel: WorkerCredentialUpdate): Either<AbstractBackendException, WorkerCredential> {
        updateModel.email?.let { email ->
            WorkerCredential.find { WorkerCredentials.email eq email }.let {
                val count = it.count()
                if (count > 1L || (count == 1L && it.singleOrNull()?.id?.value != id.value))
                    return emailOrPhoneNumberNotUnique().left()
            }
        }
        updateModel.phoneNumber?.let { phoneNumber ->
            WorkerCredential.find { WorkerCredentials.phoneNumber eq phoneNumber }.let {
                val count = it.count()
                if (count > 1L || (count == 1L && it.singleOrNull()?.id?.value != id.value))
                    return emailOrPhoneNumberNotUnique().left()
            }
        }
        return apply {
            updateModel.phoneNumber?.let { this.phoneNumber }
            updateModel.email?.let { this.email }
            updateModel.password?.let { this.password }
            this.updateDate = UpdateTime.now()
        }.right()
    }

}