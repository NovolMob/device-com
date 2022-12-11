package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.UserCreateModel
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.UserUpdateModel
import ru.novolmob.database.models.Email
import ru.novolmob.database.models.Password
import ru.novolmob.database.models.PhoneNumber
import ru.novolmob.database.models.ids.UserId

interface IUserRepository: ICrudRepository<UserId, UserModel, UserCreateModel, UserUpdateModel> {
    suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<BackendException, UserModel>
    suspend fun login(email: Email, password: Password): Either<BackendException, UserModel>
}