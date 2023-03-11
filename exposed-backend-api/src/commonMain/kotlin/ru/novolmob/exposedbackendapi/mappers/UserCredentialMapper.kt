package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.UserCredentialModel
import ru.novolmob.exposeddatabase.entities.credentials.UserCredential

class UserCredentialMapper: Mapper<UserCredential, UserCredentialModel> {
    override fun invoke(input: UserCredential): Either<AbstractBackendException, UserCredentialModel> =
        UserCredentialModel(
            userId = input.parent.id.value,
            phoneNumber = input.phoneNumber,
            email = input.email,
            password = input.password
        ).right()
}