package ru.novolmob.user_mobile_app.services

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException
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
import ru.novolmob.user_mobile_app.utils.ServiceUtil


interface IProfileService: IService {
    val profile: StateFlow<UserModel?>
    val action: StateFlow<ProfileAction?>

    suspend fun updateProfile(profileModel: UserUpdateModel): Either<AbstractBackendException, UserModel>
    suspend fun register(profileModel: UserCreateModel): Either<AbstractBackendException, UserModel>

    suspend fun loginByEmail(email: Email, password: Password): Either<AbstractBackendException, UserModel>
    suspend fun loginByPhoneNumber(phoneNumber: PhoneNumber, password: Password): Either<AbstractBackendException, UserModel>

    suspend fun logout(): Either<AbstractBackendException, Unit>

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

    private fun unauthorizedAction() = _action.update { ProfileAction.Unauthorized }
    private suspend fun loginAction(tokenModel: TokenModel? = null) {
        _action.update { ProfileAction.Login }
        tokenModel?.let {
            tokenDataStore.token(it)
        }
        ServiceUtil.updateAllServices()
    }
    private suspend fun logoutAction() {
        _action.update { ProfileAction.Logout }
        tokenDataStore.token(null)
    }
    private fun registeredAction() = _action.update { ProfileAction.Registered }
    private fun profile(profileModel: UserModel?) = _profile.update { profileModel }

    override suspend fun update(): Either<AbstractBackendException, UserModel> =
        userRepository.get().flatMap { userModel ->
            profile(userModel)
            userModel.right()
        }

    override suspend fun clear() {
        logoutAction()
    }

    override suspend fun updateProfile(profileModel: UserUpdateModel): Either<AbstractBackendException, UserModel> =
        userRepository.put(updateModel = profileModel).flatMap { newProfile ->
            ServiceUtil.updateAllServices()
            newProfile.right()
        }

    override suspend fun register(profileModel: UserCreateModel): Either<AbstractBackendException, UserModel> =
        userRepository.registration(createModel = profileModel).flatMap {
            registeredAction()
            it.right()
        }

    override suspend fun loginByEmail(
        email: Email,
        password: Password
    ): Either<AbstractBackendException, UserModel> =
        userRepository.login(email = email, password = password)
            .flatMap {
                loginAction(it)
                update()
            }

    override suspend fun loginByPhoneNumber(
        phoneNumber: PhoneNumber,
        password: Password
    ): Either<AbstractBackendException, UserModel> =
        userRepository.login(phoneNumber = phoneNumber, password = password)
            .flatMap {
                loginAction(it)
                update()
            }

    override suspend fun logout(): Either<AbstractBackendException, Unit> {
        userRepository.logout()
        logoutAction()
        ServiceUtil.clearAllServices()
        return Unit.right()
    }

    override fun get(): UserModel? = _profile.value

}



