package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.WorkerCredentialCreateModel
import ru.novolmob.backendapi.models.WorkerCredentialModel
import ru.novolmob.backendapi.models.WorkerCredentialUpdateModel
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.core.models.ids.WorkerId

interface IWorkerCredentialRepository: ICrudRepository<CredentialId, WorkerCredentialModel, WorkerCredentialCreateModel, WorkerCredentialUpdateModel> {
    suspend fun getByWorkerId(workerId: WorkerId): Either<AbstractBackendException, WorkerCredentialModel>
}