package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.getOrNull
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

class UserMapper: Mapper<ResultSet, UserModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, UserModel> =
        Either.backend {
            UserModel(
                id = input get CredentialView.UserCredentialView.id,
                firstname = input get CredentialView.UserCredentialView.firstname,
                lastname = input get CredentialView.UserCredentialView.lastname,
                patronymic = input getOrNull CredentialView.UserCredentialView.patronymic,
                birthday = input getOrNull CredentialView.UserCredentialView.birthday,
                cityId = input getOrNull CredentialView.UserCredentialView.cityId,
                language = input get CredentialView.UserCredentialView.language,
                email = input getOrNull CredentialView.UserCredentialView.email,
                phoneNumber = input get CredentialView.UserCredentialView.phoneNumber,
            )
        }
}