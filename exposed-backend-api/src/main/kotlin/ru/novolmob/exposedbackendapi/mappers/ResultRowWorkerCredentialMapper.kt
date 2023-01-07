package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.WorkerCredentialModel
import ru.novolmob.exposeddatabase.tables.credentials.WorkerCredentials

class ResultRowWorkerCredentialMapper: Mapper<ResultRow, WorkerCredentialModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, WorkerCredentialModel> =
        WorkerCredentialModel(
            id = input[WorkerCredentials.id].value,
            workerId = input[WorkerCredentials.worker].value,
            phoneNumber = input[WorkerCredentials.phoneNumber],
            email = input[WorkerCredentials.email],
            password = input[WorkerCredentials.password]
        ).right()
}