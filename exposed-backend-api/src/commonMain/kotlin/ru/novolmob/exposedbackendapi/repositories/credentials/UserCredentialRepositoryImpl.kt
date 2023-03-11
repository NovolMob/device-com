package ru.novolmob.exposedbackendapi.repositories.credentials

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.or
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.emailOrPhoneNumberNotUnique
import ru.novolmob.backendapi.exceptions.userByIdNotFound
import ru.novolmob.backendapi.exceptions.userCredentialByUserIdNotFoundException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.UserCredentialModel
import ru.novolmob.backendapi.models.UserCredentialUpdate
import ru.novolmob.backendapi.repositories.IUserCredentialRepository
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.exposeddatabase.entities.User
import ru.novolmob.exposeddatabase.entities.credentials.UserCredential
import ru.novolmob.exposeddatabase.tables.credentials.UserCredentials

class UserCredentialRepositoryImpl(
    mapper: Mapper<UserCredential, UserCredentialModel>,
): IUserCredentialRepository, AbstractCredentialRepository<UserCredential.Companion, UserCredential, User, UserId, UserCredentialModel, UserCredentialUpdate>(
    UserCredential.Companion, mapper, ::userCredentialByUserIdNotFoundException
) {

    override suspend fun UserCredential.Companion.new(createModel: UserCredentialModel): Either<AbstractBackendException, UserCredential> {
        if (!UserCredential.find { (UserCredentials.email eq createModel.email) or (UserCredentials.phoneNumber eq createModel.phoneNumber) }.empty())
            return emailOrPhoneNumberNotUnique().left()
        val user = User.findById(createModel.userId) ?: return userByIdNotFound(createModel.userId).left()
        return new {
            this.parent = user
            this.phoneNumber = createModel.phoneNumber
            this.email = createModel.email
            this.password = createModel.password
        }.right()
    }

    override suspend fun UserCredential.apply(updateModel: UserCredentialUpdate): Either<AbstractBackendException, UserCredential> {
        updateModel.email?.let { email ->
            UserCredential.find { UserCredentials.email eq email }.let {
                val count = it.count()
                if (count > 1L || (count == 1L && it.singleOrNull()?.id?.value != id.value))
                    return emailOrPhoneNumberNotUnique().left()
            }
        }
        updateModel.phoneNumber?.let { phoneNumber ->
            UserCredential.find { UserCredentials.phoneNumber eq phoneNumber }.let {
                val count = it.count()
                if (count > 1L || (count == 1L && it.singleOrNull()?.id?.value != id.value))
                    return emailOrPhoneNumberNotUnique().left()
            }
        }
        return apply {
            updateModel.phoneNumber?.let { this.phoneNumber }
            updateModel.email?.let { this.email }
            updateModel.password?.let { this.password }
            this.updateDate = UpdateTime.now()
        }.right()
    }
}