package ru.novolmob.user_mobile_app.services

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.TokenModel
import ru.novolmob.backendapi.models.UserCreateModel
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.UserUpdateModel
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.user_mobile_app.datastore.TokenDataStore
import ru.novolmob.user_mobile_app.models.ProfileAction
import ru.novolmob.user_mobile_app.repositories.IUserRepository


interface IProfileService: IService {
    val profile: StateFlow<UserModel?>
    val action: StateFlow<ProfileAction?>

    suspend fun updateProfile(profileModel: UserUpdateModel): Either<BackendException, UserModel>
    suspend fun register(profileModel: UserCreateModel): Either<BackendException, UserModel>

    suspend fun loginByEmail(email: Email, password: Password): Either<BackendException, UserModel>
    suspend fun loginByPhoneNumber(phoneNumber: PhoneNumber, password: Password): Either<BackendException, UserModel>

    suspend fun logout(): Either<BackendException, Unit>

    fun get(): UserModel?
}

class ProfileServiceImpl(
    val userRepository: IUserRepository,
    val tokenDataStore: TokenDataStore
): IProfileService {
    private val _profile = MutableStateFlow<UserModel?>(null)
    override val profile: StateFlow<UserModel?> = _profile.asStateFlow()

    private val _action = MutableStateFlow<ProfileAction?>(null)
    override val action: StateFlow<ProfileAction?> = _action.asStateFlow()

    init {
        serviceScope.launch {
            update().fold(
                ifLeft = {
                    unauthorizedAction()
                },
                ifRight = {
                    loginAction()
                }
            )
        }
    }

    private suspend fun unauthorizedAction() = _action.emit(ProfileAction.Unauthorized)
    private suspend fun loginAction(tokenModel: TokenModel? = null) {
        _action.emit(ProfileAction.Login)
        tokenModel?.let {
            tokenDataStore.token(it)
        }
    }
    private suspend fun logoutAction() {
        _action.emit(ProfileAction.Logout)
        tokenDataStore.token(null)
    }
    private suspend fun registeredAction() = _action.emit(ProfileAction.Registered)
    private suspend fun profile(profileModel: UserModel?) = _profile.emit(profileModel)

    override suspend fun update(): Either<BackendException, UserModel> =
        userRepository.get().flatMap { userModel ->
            profile(userModel)
            userModel.right()
        }

    override suspend fun updateProfile(profileModel: UserUpdateModel): Either<BackendException, UserModel> =
        userRepository.put(updateModel = profileModel).flatMap { newProfile ->
            profile(newProfile)
            newProfile.right()
        }

    override suspend fun register(profileModel: UserCreateModel): Either<BackendException, UserModel> =
        userRepository.registration(createModel = profileModel).flatMap {
            registeredAction()
            it.right()
        }

    override suspend fun loginByEmail(
        email: Email,
        password: Password
    ): Either<BackendException, UserModel> =
        userRepository.login(email = email, password = password)
            .flatMap {
                loginAction(it)
                update()
            }

    override suspend fun loginByPhoneNumber(
        phoneNumber: PhoneNumber,
        password: Password
    ): Either<BackendException, UserModel> =
        userRepository.login(phoneNumber = phoneNumber, password = password)
            .flatMap {
                loginAction(it)
                update()
            }

    override suspend fun logout(): Either<BackendException, Unit> =
        userRepository.logout().flatMap {
            logoutAction()
            Unit.right()
        }

    override fun get(): UserModel? = _profile.value

}



