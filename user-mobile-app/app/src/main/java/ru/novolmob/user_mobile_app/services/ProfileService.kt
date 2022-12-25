package ru.novolmob.user_mobile_app.services

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.user_mobile_app.models.ProfileAction
import ru.novolmob.user_mobile_app.models.ProfileModel
import ru.novolmob.user_mobile_app.models.ProfileUpdateModel


interface IProfileService: IService {
    val profile: StateFlow<ProfileModel?>
    val action: StateFlow<ProfileAction?>

    suspend fun updateProfile(profileModel: ProfileUpdateModel): Either<BackendException, ProfileModel>
    suspend fun register(profileModel: ProfileModel, password: String): Either<BackendException, ProfileModel>

    suspend fun loginByEmail(email: String, password: String): Either<BackendException, Boolean>
    suspend fun loginByPhoneNumber(phoneNumber: String, password: String): Either<BackendException, Boolean>

    suspend fun logout(): Either<BackendException, Boolean>

    fun get(): ProfileModel
}

class ProfileServiceImpl: IProfileService {
    private val _profile = MutableStateFlow<ProfileModel?>(null)
    override val profile: StateFlow<ProfileModel?> = _profile.asStateFlow()

    private val _action = MutableStateFlow<ProfileAction?>(null)
    override val action: StateFlow<ProfileAction?> = _action.asStateFlow()

    init {
        serviceScope.launch {
            update()
        }
    }

    private fun loginAction() = _action.update { ProfileAction.Login }
    private fun logoutAction() = _action.update { ProfileAction.Logout }

    override suspend fun update(): Either<BackendException, Unit> = Unit.right()

    override suspend fun updateProfile(profileModel: ProfileUpdateModel): Either<BackendException, ProfileModel> {
        return _profile.value?.let {
            ProfileModel(
                firstname = profileModel.firstname ?: it.firstname,
                lastname = profileModel.lastname ?: it.lastname,
                patronymic = profileModel.patronymic ?: it.patronymic,
                birthday = profileModel.birthday ?: it.birthday,
                city = profileModel.city ?: it.city,
                language = profileModel.language ?: it.language,
                email = profileModel.email ?: it.email,
                phoneNumber = profileModel.phoneNumber ?: it.phoneNumber,
            ).let { newProfile ->
                _profile.update { newProfile }
                newProfile.right()
            }
        } ?: BackendException(code = BackendExceptionCode.NOT_FOUND, "Пользователь пуст").left()
    }

    override suspend fun register(profileModel: ProfileModel, password: String): Either<BackendException, ProfileModel> {
        delay(2000)
        _profile.update { profileModel }
        loginAction()
        return profileModel.right()
    }

    override suspend fun loginByEmail(
        email: String,
        password: String
    ): Either<BackendException, Boolean> {
        delay(2000)
        _profile.emit(ProfileModel())
        loginAction()
        return true.right()
    }

    override suspend fun loginByPhoneNumber(
        phoneNumber: String,
        password: String
    ): Either<BackendException, Boolean> {
        delay(2000)
        _profile.emit(ProfileModel())
        loginAction()
        return true.right()
    }

    override suspend fun logout(): Either<BackendException, Boolean> {
        _profile.update { null }
        logoutAction()
        return true.right()
    }

    override fun get(): ProfileModel = _profile.value ?: throw Exception("Profile not found!")

}



