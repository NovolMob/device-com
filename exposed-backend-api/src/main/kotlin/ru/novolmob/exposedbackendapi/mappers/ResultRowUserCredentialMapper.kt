package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.UserCredentialModel
import ru.novolmob.exposeddatabase.tables.credentials.UserCredentials

class ResultRowUserCredentialMapper: Mapper<ResultRow, UserCredentialModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, UserCredentialModel> =
        UserCredentialModel(
            id = input[UserCredentials.id].value,
            userId = input[UserCredentials.user].value,
            phoneNumber = input[UserCredentials.phoneNumber],
            email = input[UserCredentials.email],
            password = input[UserCredentials.password]
        ).right()
}