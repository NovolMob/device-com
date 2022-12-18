package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.WorkerCredentialModel
import ru.novolmob.exposeddatabase.entities.WorkerCredential

class WorkerCredentialMapper: Mapper<WorkerCredential, WorkerCredentialModel> {
    override fun invoke(input: WorkerCredential): Either<BackendException, WorkerCredentialModel> =
        WorkerCredentialModel(
            id = input.id.value,
            workerId = input.worker.id.value,
            phoneNumber = input.phoneNumber,
            email = input.email,
            password = input.password
        ).right()
}