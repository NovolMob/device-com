package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.exposedbackendapi.exceptions.userCredentialByUserIdNotFoundException
import ru.novolmob.exposeddatabase.entities.User

class UserMapper: Mapper<User, UserModel> {
    override fun invoke(input: User): Either<BackendException, UserModel> =
        input.credentials.firstOrNull()?.let {
            UserModel(
                id = input.id.value,
                firstname = input.firstname,
                lastname = input.lastname,
                patronymic = input.patronymic,
                birthday = input.birthday,
                city = input.city,
                language = input.language,
                phoneNumber = it.phoneNumber,
                email = it.email
            ).right()
        } ?: userCredentialByUserIdNotFoundException(input.id.value).left()
}