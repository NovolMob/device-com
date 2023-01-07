package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.exposedbackendapi.exceptions.userByIdNotFound
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IUserCredentialRepository
import ru.novolmob.backendapi.repositories.IUserRepository
import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.exposedbackendapi.exceptions.cityByIdNotFound
import ru.novolmob.exposedbackendapi.exceptions.userCredentialByUserIdNotFoundException
import ru.novolmob.exposeddatabase.entities.City
import ru.novolmob.exposeddatabase.entities.User
import ru.novolmob.exposeddatabase.entities.UserCredential
import ru.novolmob.exposeddatabase.tables.Users
import ru.novolmob.exposeddatabase.tables.credentials.UserCredentials

class UserRepositoryImpl(
    val mapper: Mapper<User, UserModel>,
    val resultRowInfoMapper: Mapper<ResultRow, UserInfoModel>,
    val userCredentialRepository: IUserCredentialRepository
): IUserRepository {
    override suspend fun getLanguage(userId: UserId): Either<AbstractBackendException, Language> =
        newSuspendedTransaction(Dispatchers.IO) {
            User.findById(userId)?.language?.right() ?: userByIdNotFound(userId).left()
        }

    override suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<AbstractBackendException, UserModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            UserCredential.find { (UserCredentials.phoneNumber eq phoneNumber) and (UserCredentials.password eq password) }
                .limit(1).firstOrNull()?.let {
                    mapper(it.user)
                } ?: ru.novolmob.exposedbackendapi.exceptions.badCredentialsException().left()
        }

    override suspend fun login(email: Email, password: Password): Either<AbstractBackendException, UserModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            UserCredential.find { (UserCredentials.email eq email) and (UserCredentials.password eq password) }
                .limit(1).firstOrNull()?.let {
                    mapper(it.user)
                } ?: ru.novolmob.exposedbackendapi.exceptions.badCredentialsException().left()
        }

    override suspend fun get(id: UserId): Either<AbstractBackendException, UserModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            User.findById(id)?.let(mapper::invoke) ?: userByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<UserModel>> =
        RepositoryUtil.generalGetAll(Users, pagination, resultRowInfoMapper).flatMap { page ->
            val list = page.list.parTraverseEither { userInfo ->
                userCredentialRepository.getByUserId(userInfo.id).flatMap { credential ->
                    UserModel(
                        id = userInfo.id,
                        firstname = userInfo.firstname,
                        lastname = userInfo.lastname,
                        patronymic = userInfo.patronymic,
                        birthday = userInfo.birthday,
                        cityId = userInfo.cityId,
                        language = userInfo.language,
                        phoneNumber = credential.phoneNumber,
                        email = credential.email
                    ).right()
                }
            }
            list.flatMap { Page(page = page.page, size = page.size, list = it).right() }
        }

    override suspend fun post(createModel: UserCreateModel): Either<AbstractBackendException, UserModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val city = createModel.cityId?.let {
                City.findById(it) ?: return@newSuspendedTransaction cityByIdNotFound(it).left()
            }
            User.new {
                this.firstname = createModel.firstname
                this.lastname = createModel.lastname
                this.patronymic = createModel.patronymic
                this.birthday = createModel.birthday
                this.city = city
                this.language = createModel.language
            }.also {
                commit()
                userCredentialRepository.post(
                    UserCredentialCreateModel(
                        userId = it.id.value,
                        phoneNumber = createModel.phoneNumber,
                        email = createModel.email,
                        password = createModel.password
                    )
                )
            }.let(mapper::invoke)
        }

    override suspend fun post(id: UserId, createModel: UserCreateModel): Either<AbstractBackendException, UserModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val city = createModel.cityId?.let {
                City.findById(it) ?: return@newSuspendedTransaction cityByIdNotFound(it).left()
            }
            val user = User.findById(id) ?: return@newSuspendedTransaction userByIdNotFound(id).left()
            UserCredential.find { UserCredentials.user eq id }.limit(1).singleOrNull()?.apply {
                this.email = createModel.email
                this.phoneNumber = createModel.phoneNumber
                this.password = createModel.password
                this.updateDate = UpdateTime.now()
            } ?: return@newSuspendedTransaction userCredentialByUserIdNotFoundException(id).left()
            user.apply {
                this.firstname = createModel.firstname
                this.lastname = createModel.lastname
                this.patronymic = createModel.patronymic
                this.birthday = createModel.birthday
                this.city = city
                this.language = createModel.language
                this.updateDate = UpdateTime.now()
            }.let(mapper::invoke)
        }

    override suspend fun put(id: UserId, updateModel: UserUpdateModel): Either<AbstractBackendException, UserModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val city = updateModel.cityId?.let {
                City.findById(it) ?: return@newSuspendedTransaction cityByIdNotFound(it).left()
            }
            val user = User.findById(id) ?: return@newSuspendedTransaction userByIdNotFound(id).left()
            UserCredential.find { UserCredentials.user eq id }.limit(1).singleOrNull()?.apply {
                updateModel.email?.let { this.email = it }
                updateModel.phoneNumber?.let { this.phoneNumber = it }
                updateModel.password?.let { this.password = it }
                this.updateDate = UpdateTime.now()
            } ?: return@newSuspendedTransaction userCredentialByUserIdNotFoundException(id).left()
            user.apply {
                updateModel.firstname?.let { this.firstname = it }
                updateModel.lastname?.let { this.lastname = it }
                updateModel.patronymic?.let { this.patronymic = it }
                updateModel.birthday?.let { this.birthday = it }
                city?.let { this.city = it }
                updateModel.language?.let { this.language = it }
                this.updateDate = UpdateTime.now()
            }.let(mapper::invoke)
        }

    override suspend fun delete(id: UserId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            User.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

}