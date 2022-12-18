package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.UserCredentialCreateModel
import ru.novolmob.backendapi.models.UserCredentialModel
import ru.novolmob.backendapi.models.UserCredentialUpdateModel
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.core.models.ids.UserId

interface IUserCredentialRepository: ICrudRepository<CredentialId, UserCredentialModel, UserCredentialCreateModel, UserCredentialUpdateModel> {
    suspend fun getByUserId(userId: UserId): Either<BackendException, UserCredentialModel>
}