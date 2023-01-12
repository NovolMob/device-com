package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.WorkerCredentialModel
import ru.novolmob.exposeddatabase.entities.credentials.WorkerCredential

class WorkerCredentialMapper: Mapper<WorkerCredential, WorkerCredentialModel> {
    override fun invoke(input: WorkerCredential): Either<AbstractBackendException, WorkerCredentialModel> =
        WorkerCredentialModel(
            workerId = input.parent.id.value,
            phoneNumber = input.phoneNumber,
            email = input.email,
            password = input.password
        ).right()
}