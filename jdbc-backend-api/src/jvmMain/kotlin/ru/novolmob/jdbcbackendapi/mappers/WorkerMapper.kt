package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.getOrNull
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

class WorkerMapper: Mapper<ResultSet, WorkerModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, WorkerModel> =
        Either.backend {
            WorkerModel(
                id = input get CredentialView.WorkerCredentialView.id,
                pointId = input getOrNull CredentialView.WorkerCredentialView.pointId,
                firstname = input get CredentialView.WorkerCredentialView.firstname,
                lastname = input get CredentialView.WorkerCredentialView.lastname,
                patronymic = input getOrNull CredentialView.WorkerCredentialView.patronymic,
                language = input get CredentialView.WorkerCredentialView.language,
                email = input getOrNull CredentialView.WorkerCredentialView.email,
                phoneNumber = input get CredentialView.WorkerCredentialView.phoneNumber,
            )
        }
}