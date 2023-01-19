package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import ru.novolmob.backendapi.exceptions.*
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.WorkerCreateModel
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.backendapi.models.WorkerUpdateModel
import ru.novolmob.backendapi.repositories.IWorkerRepository
import ru.novolmob.backendapi.transformations.Transformation
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.list
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateWorkerFunction
import ru.novolmob.jdbcdatabase.functions.LoginByEmailFunction
import ru.novolmob.jdbcdatabase.functions.LoginByPhoneNumberFunction
import ru.novolmob.jdbcdatabase.tables.Credentials
import ru.novolmob.jdbcdatabase.tables.Workers
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

class WorkerRepositoryImpl(
    mapper: Mapper<ResultSet, WorkerModel>,
    val passwordTransformation: Transformation<Password>
): IWorkerRepository, AbstractCrudCredentialViewRepository<WorkerId, WorkerModel, WorkerCreateModel, WorkerUpdateModel>(
    CredentialView.WorkerCredentialView, mapper, ::workerByIdNotFound
) {
    override suspend fun getAllByPointId(pointId: PointId): Either<AbstractBackendException, List<WorkerModel>> =
        CredentialView.WorkerCredentialView.select(pointId) { list(mapper) }

    override suspend fun login(
        phoneNumber: PhoneNumber,
        password: Password
    ): Either<AbstractBackendException, WorkerModel> =
        LoginByPhoneNumberFunction.WorkerLoginByPhoneNumberFunction.call(phoneNumber, passwordTransformation(password)) { fold(ifEmpty = { badCredentialsException() }, mapper::invoke) }

    override suspend fun login(email: Email, password: Password): Either<AbstractBackendException, WorkerModel> =
        LoginByEmailFunction.WorkerLoginByEmailFunction.call(email, passwordTransformation(password)) { fold(ifEmpty = { badCredentialsException() }, mapper::invoke) }

    override suspend fun post(createModel: WorkerCreateModel): Either<AbstractBackendException, WorkerModel> {
        if (!Credentials.WorkerCredentials.check(email = createModel.email, phoneNumber = createModel.phoneNumber))
            return emailOrPhoneNumberNotUnique().left()
        return CreationOrUpdateWorkerFunction.call(
            pointId = createModel.pointId,
            firstname = createModel.firstname,
            lastname = createModel.lastname,
            patronymic = createModel.patronymic,
            language = createModel.language,
            email = createModel.email,
            phoneNumber = createModel.phoneNumber,
            password = passwordTransformation(createModel.password),
        ) { fold(ifEmpty = { failedToCreateWorker() }, mapper::invoke) }
    }

    override suspend fun post(
        id: WorkerId,
        createModel: WorkerCreateModel
    ): Either<AbstractBackendException, WorkerModel> {
        if (!Credentials.WorkerCredentials.check(id, createModel.email, createModel.phoneNumber))
            return emailOrPhoneNumberNotUnique().left()
        return CreationOrUpdateWorkerFunction.call(
            workerId = id,
            pointId = createModel.pointId,
            firstname = createModel.firstname,
            lastname = createModel.lastname,
            patronymic = createModel.patronymic,
            language = createModel.language,
            email = createModel.email,
            phoneNumber = createModel.phoneNumber,
            password = passwordTransformation(createModel.password),
        ) { fold(ifEmpty = { failedToCreateWorker() }, mapper::invoke) }
    }

    override suspend fun put(
        id: WorkerId,
        updateModel: WorkerUpdateModel
    ): Either<AbstractBackendException, WorkerModel> {
        return Either.backend {
            if (listOf(updateModel.email, updateModel.phoneNumber, updateModel.password).any { it != null }) {
                if (!Credentials.WorkerCredentials.check(id, updateModel.email, updateModel.phoneNumber))
                    return emailOrPhoneNumberNotUnique().left()
                Credentials.WorkerCredentials.update(
                    ownerId = id, email = updateModel.email, phoneNumber = updateModel.phoneNumber,
                    password = updateModel.password?.let(passwordTransformation::invoke)
                )
            }
            if (listOf(
                    updateModel.pointId,updateModel.firstname, updateModel.lastname,
                    updateModel.patronymic, updateModel.language
                ).any { it != null }) {
                Workers.update(
                    id = id, pointId = updateModel.pointId, firstname = updateModel.firstname,
                    lastname = updateModel.lastname, patronymic = updateModel.patronymic,
                    language = updateModel.language
                )
            }
        }.flatMap { get(id) }
    }

    override suspend fun delete(id: WorkerId): Either<AbstractBackendException, Boolean> =
        (Workers.delete(id) > 0).right()
}