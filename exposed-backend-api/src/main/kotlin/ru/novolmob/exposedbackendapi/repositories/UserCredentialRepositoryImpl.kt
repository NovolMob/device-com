package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.exposedbackendapi.exceptions.userByIdNotFound
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IUserCredentialRepository
import ru.novolmob.core.models.UpdateDate
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.exposeddatabase.entities.User
import ru.novolmob.exposeddatabase.entities.UserCredential
import ru.novolmob.exposeddatabase.tables.credentials.UserCredentials

class UserCredentialRepositoryImpl(
    val mapper: Mapper<UserCredential, UserCredentialModel>,
    val resultRowMapper: Mapper<ResultRow, UserCredentialModel>
): IUserCredentialRepository {
    override suspend fun getByUserId(userId: UserId): Either<BackendException, UserCredentialModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            UserCredential.find { UserCredentials.user eq userId }
                .limit(1).firstOrNull()?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.userCredentialByUserIdNotFoundException(
                userId
            ).left()
        }

    override suspend fun get(id: CredentialId): Either<BackendException, UserCredentialModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            UserCredential.findById(id)?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.credentialByIdNotFoundException(
                id
            ).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<UserCredentialModel>> =
        RepositoryUtil.generalGatAll(UserCredentials, pagination, resultRowMapper)

    override suspend fun post(createModel: UserCredentialCreateModel): Either<BackendException, UserCredentialModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction userByIdNotFound(createModel.userId).left()
            UserCredential.new {
                this.user = user
                this.phoneNumber = createModel.phoneNumber
                this.email = createModel.email
                this.password = createModel.password
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: CredentialId,
        createModel: UserCredentialCreateModel
    ): Either<BackendException, UserCredentialModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction userByIdNotFound(createModel.userId).left()
            UserCredential.findById(id)?.apply {
                this.user = user
                this.phoneNumber = createModel.phoneNumber
                this.email = createModel.email
                this.password = createModel.password
            }?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.credentialByIdNotFoundException(id).left()
        }

    override suspend fun put(
        id: CredentialId,
        updateModel: UserCredentialUpdateModel
    ): Either<BackendException, UserCredentialModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = updateModel.userId?.let {
                User.findById(it) ?: return@newSuspendedTransaction userByIdNotFound(it).left()
            }
            UserCredential.findById(id)?.apply {
                user?.let { this.user = it }
                updateModel.phoneNumber?.let { this.phoneNumber }
                updateModel.email?.let { this.email }
                updateModel.password?.let { this.password }
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.credentialByIdNotFoundException(id).left()
        }

    override suspend fun delete(id: CredentialId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            UserCredential.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}