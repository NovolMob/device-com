package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.WorkerCredentialCreateModel
import ru.novolmob.backendapi.models.WorkerCredentialModel
import ru.novolmob.backendapi.models.WorkerCredentialUpdateModel
import ru.novolmob.database.models.ids.CredentialId
import ru.novolmob.database.models.ids.WorkerId

interface IWorkerCredentialRepository: ICrudRepository<CredentialId, WorkerCredentialModel, WorkerCredentialCreateModel, WorkerCredentialUpdateModel> {
    suspend fun getByWorkerId(workerId: WorkerId): Either<BackendException, WorkerCredentialModel>
}