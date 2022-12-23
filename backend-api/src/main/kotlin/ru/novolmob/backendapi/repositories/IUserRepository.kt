package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.UserCreateModel
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.UserUpdateModel
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.models.ids.UserId

interface IUserRepository: ICrudRepository<UserId, UserModel, UserCreateModel, UserUpdateModel> {
    suspend fun getLanguage(userId: UserId): Either<BackendException, Language>
    suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<BackendException, UserModel>
    suspend fun login(email: Email, password: Password): Either<BackendException, UserModel>
}