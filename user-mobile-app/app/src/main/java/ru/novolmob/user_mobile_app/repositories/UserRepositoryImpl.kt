package ru.novolmob.user_mobile_app.repositories

import arrow.core.Either
import io.ktor.client.*
import org.koin.core.component.KoinComponent
import ru.novolmob.backend.ktorrouting.user.Login
import ru.novolmob.backend.ktorrouting.user.Logout
import ru.novolmob.backend.ktorrouting.user.Registration
import ru.novolmob.backend.ktorrouting.user.User
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.user_mobile_app.utils.KtorUtil.get
import ru.novolmob.user_mobile_app.utils.KtorUtil.post
import ru.novolmob.user_mobile_app.utils.KtorUtil.put

interface IUserRepository {
    suspend fun logout(): Either<BackendException, Unit>
    suspend fun login(email: Email, password: Password): Either<BackendException, TokenModel>
    suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<BackendException, TokenModel>
    suspend fun get(): Either<BackendException, UserModel>
    suspend fun registration(createModel: UserCreateModel): Either<BackendException, UserModel>
    suspend fun put(updateModel: UserUpdateModel): Either<BackendException, UserModel>
}

class UserRepositoryImpl(
    private val client: HttpClient
): KoinComponent, IUserRepository {

    override suspend fun logout(): Either<BackendException, Unit> =
        client.post(resource = Logout())

    override suspend fun login(
        email: Email,
        password: Password
    ): Either<BackendException, TokenModel> =
        client.post(
            resource = Login(),
            body = LoginModel(email = email, password = password)
        )

    override suspend fun login(
        phoneNumber: PhoneNumber,
        password: Password
    ): Either<BackendException, TokenModel> =
        client.post(
            resource = Login(),
            body = LoginModel(phoneNumber = phoneNumber, password = password)
        )

    override suspend fun get(): Either<BackendException, UserModel> =
        client.get(resource = User())

    override suspend fun registration(createModel: UserCreateModel): Either<BackendException, UserModel> =
        client.post(resource = Registration(), body = createModel)

    override suspend fun put(updateModel: UserUpdateModel): Either<BackendException, UserModel> =
        client.put(resource = User(), body = updateModel)
}