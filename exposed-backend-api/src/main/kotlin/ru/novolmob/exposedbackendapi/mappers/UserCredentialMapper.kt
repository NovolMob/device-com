package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.UserCredentialModel
import ru.novolmob.exposeddatabase.entities.UserCredential

class UserCredentialMapper: Mapper<UserCredential, UserCredentialModel> {
    override fun invoke(input: UserCredential): Either<BackendException, UserCredentialModel> =
        UserCredentialModel(
            id = input.id.value,
            userId = input.user.id.value,
            phoneNumber = input.phoneNumber,
            email = input.email,
            password = input.password
        ).right()
}