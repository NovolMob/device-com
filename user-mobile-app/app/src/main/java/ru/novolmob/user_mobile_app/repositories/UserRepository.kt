package ru.novolmob.user_mobile_app.repositories

import arrow.core.Either
import io.ktor.client.*
import org.koin.core.component.KoinComponent
import ru.novolmob.backend.ktorrouting.user.Login
import ru.novolmob.backend.ktorrouting.user.Logout
import ru.novolmob.backend.ktorrouting.user.Registration
import ru.novolmob.backend.ktorrouting.user.User
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.user_mobile_app.utils.KtorUtil.get
import ru.novolmob.user_mobile_app.utils.KtorUtil.post
import ru.novolmob.user_mobile_app.utils.KtorUtil.put

interface IUserRepository: IRepository {
    suspend fun logout(): Either<AbstractBackendException, Unit>
    suspend fun login(email: Email, password: Password): Either<AbstractBackendException, TokenModel>
    suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<AbstractBackendException, TokenModel>
    suspend fun get(): Either<AbstractBackendException, UserModel>
    suspend fun registration(createModel: UserCreateModel): Either<AbstractBackendException, UserModel>
    suspend fun put(updateModel: UserUpdateModel): Either<AbstractBackendException, UserModel>
}

class UserRepositoryImpl(
    private val client: HttpClient
): KoinComponent, IUserRepository {

    override suspend fun logout(): Either<AbstractBackendException, Unit> =
        client.post(resource = Logout())

    override suspend fun login(
        email: Email,
        password: Password
    ): Either<AbstractBackendException, TokenModel> =
        client.post(
            resource = Login(),
            body = LoginModel(email = email, password = password)
        )

    override suspend fun login(
        phoneNumber: PhoneNumber,
        password: Password
    ): Either<AbstractBackendException, TokenModel> =
        client.post(
            resource = Login(),
            body = LoginModel(phoneNumber = phoneNumber, password = password)
        )

    override suspend fun get(): Either<AbstractBackendException, UserModel> =
        client.get(resource = User())

    override suspend fun registration(createModel: UserCreateModel): Either<AbstractBackendException, UserModel> =
        client.post(resource = Registration(), body = createModel)

    override suspend fun put(updateModel: UserUpdateModel): Either<AbstractBackendException, UserModel> =
        client.put(resource = User(), body = updateModel)
}