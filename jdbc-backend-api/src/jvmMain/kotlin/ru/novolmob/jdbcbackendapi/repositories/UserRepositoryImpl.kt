package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import ru.novolmob.backendapi.exceptions.*
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.UserCreateModel
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.UserUpdateModel
import ru.novolmob.backendapi.repositories.IUserRepository
import ru.novolmob.backendapi.transformations.Transformation
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateUserFunction
import ru.novolmob.jdbcdatabase.functions.LoginByEmailFunction
import ru.novolmob.jdbcdatabase.functions.LoginByPhoneNumberFunction
import ru.novolmob.jdbcdatabase.tables.Credentials
import ru.novolmob.jdbcdatabase.tables.Users
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

class UserRepositoryImpl(
    mapper: Mapper<ResultSet, UserModel>,
    val passwordTransformation: Transformation<Password>
): IUserRepository, AbstractCrudCredentialViewRepository<UserId, UserModel, UserCreateModel, UserUpdateModel>(
    CredentialView.UserCredentialView, mapper, ::userByIdNotFound
) {
    override suspend fun login(
        phoneNumber: PhoneNumber,
        password: Password
    ): Either<AbstractBackendException, UserModel> =
        LoginByPhoneNumberFunction.UserLoginByPhoneNumberFunction.call(phoneNumber, passwordTransformation(password)) { fold(ifEmpty = { badCredentialsException() }, mapper::invoke) }

    override suspend fun login(email: Email, password: Password): Either<AbstractBackendException, UserModel> =
        LoginByEmailFunction.UserLoginByEmailFunction.call(email, passwordTransformation(password)) { fold(ifEmpty = { badCredentialsException() }, mapper::invoke) }

    override suspend fun post(createModel: UserCreateModel): Either<AbstractBackendException, UserModel> {
        if (!Credentials.UserCredentials.check(email = createModel.email, phoneNumber = createModel.phoneNumber))
            return emailOrPhoneNumberNotUnique().left()
        return CreationOrUpdateUserFunction.call(
            firstname = createModel.firstname,
            lastname = createModel.lastname,
            patronymic = createModel.patronymic,
            birthday = createModel.birthday,
            cityId = createModel.cityId,
            language = createModel.language,
            email = createModel.email,
            phoneNumber = createModel.phoneNumber,
            password = passwordTransformation(createModel.password),
        ) { fold(ifEmpty = { failedToCreateUser() }, mapper::invoke) }
    }

    override suspend fun post(id: UserId, createModel: UserCreateModel): Either<AbstractBackendException, UserModel> {
        if (!Credentials.UserCredentials.check(id, createModel.email, createModel.phoneNumber))
            return emailOrPhoneNumberNotUnique().left()
        return CreationOrUpdateUserFunction.call(
            userId = id,
            firstname = createModel.firstname,
            lastname = createModel.lastname,
            patronymic = createModel.patronymic,
            birthday = createModel.birthday,
            cityId = createModel.cityId,
            language = createModel.language,
            email = createModel.email,
            phoneNumber = createModel.phoneNumber,
            password = passwordTransformation(createModel.password),
        ) { fold(ifEmpty = { failedToCreateUser() }, mapper::invoke) }
    }

    override suspend fun put(id: UserId, updateModel: UserUpdateModel): Either<AbstractBackendException, UserModel> {
        return Either.backend {
            if (listOf(updateModel.email, updateModel.phoneNumber, updateModel.password).any { it != null }) {
                if (!Credentials.UserCredentials.check(id, updateModel.email, updateModel.phoneNumber))
                    return emailOrPhoneNumberNotUnique().left()
                Credentials.UserCredentials.update(
                    ownerId = id, email = updateModel.email, phoneNumber = updateModel.phoneNumber, password = updateModel.password?.let(passwordTransformation::invoke)
                )
            }
            if (listOf(
                    updateModel.firstname, updateModel.lastname, updateModel.patronymic,
                    updateModel.birthday, updateModel.cityId, updateModel.language
            ).any { it != null }) {
                Users.update(
                    id = id, firstname = updateModel.firstname, lastname = updateModel.lastname,
                    patronymic = updateModel.patronymic, birthday = updateModel.birthday,
                    cityId = updateModel.cityId, language = updateModel.language
                )
            }
        }.flatMap { get(id) }
    }

    override suspend fun delete(id: UserId): Either<AbstractBackendException, Boolean> =
        (Users.delete(id) > 0).right()
}