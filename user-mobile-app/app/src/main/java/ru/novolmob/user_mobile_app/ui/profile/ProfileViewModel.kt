package ru.novolmob.user_mobile_app.ui.profile

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.user_mobile_app.models.ProfileModel
import ru.novolmob.user_mobile_app.models.ProfileUpdateModel
import ru.novolmob.user_mobile_app.models.ScreenNotification
import ru.novolmob.user_mobile_app.services.IProfileService
import ru.novolmob.user_mobile_app.ui.registration.RUSSIANS
import java.time.LocalDate
import java.util.*

data class ProfileState(
    val avatar: ImageBitmap? = null,
    val currentProfile: ProfileModel,
    val firstname: String? = null,
    val lastname: String? = null,
    val patronymic: String? = null,
    val birthday: LocalDate? = null,
    val availableCities: List<String> = listOf(
        "Великий Новгород"
    ),
    val city: String? = null,
    val availableLanguages: List<Locale> = listOf(
        RUSSIANS
    ),
    val language: Locale? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null,
    val canSave: Boolean = false,
    val loading: Boolean = false,
    val logout: Boolean = false
)

class ProfileViewModel(
    private val profileService: IProfileService,
    private val emailRegex: Regex = Regex("\\w+@\\w+.\\w+"),
    private val phoneNumberRegex: Regex = Regex("\\+\\d+"),
): ViewModel() {
    private val _state = MutableStateFlow(ProfileState(currentProfile = profileService.get()))
    val state = _state.asStateFlow()

    private fun ProfileState.updateCanSave() = copy(
        canSave = firstname?.isNotEmpty() == true  || lastname?.isNotEmpty() == true
                || patronymic?.isNotEmpty() == true || password?.isNotEmpty() == true
                || language != null || city?.isNotEmpty() == true
                || (birthday != null && validBirthday(birthday))
                || (phoneNumber?.isNotEmpty() == true && validPhoneNumber(phoneNumber))
                || (email?.isNotEmpty() == true && validEmail(email))
    )

    fun firstname(firstname: String?) =
        _state.update {
            it.copy(firstname = firstname).updateCanSave()
        }

    fun lastname(lastname: String?) =
        _state.update {
            it.copy(lastname = lastname).updateCanSave()
        }

    fun patronymic(patronymic: String?) =
        _state.update {
            it.copy(patronymic = patronymic).updateCanSave()
        }

    fun birthday(birthday: LocalDate?) =
        _state.update {
            it.copy(birthday = birthday).updateCanSave()
        }

    fun city(city: String?) =
        _state.update {
            it.copy(city = city).updateCanSave()
        }

    fun language(language: Locale?) =
        _state.update {
            it.copy(language = language).updateCanSave()
        }

    fun email(email: String?) =
        _state.update {
            it.copy(email = email).updateCanSave()
        }

    fun phoneNumber(phoneNumber: String?) =
        _state.update {
            it.copy(phoneNumber = phoneNumber).updateCanSave()
        }

    fun password(password: String?) =
        _state.update {
            it.copy(password = password).updateCanSave()
        }


    fun validEmail(email: String): Boolean = emailRegex.matches(email)
    fun validBirthday(birthday: LocalDate): Boolean = birthday < LocalDate.now()
    fun validPhoneNumber(phoneNumber: String): Boolean = phoneNumberRegex.matches(phoneNumber)

    fun saveProfile() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            delay(2000)
            viewModelScope.launch {
                _state.value.run {
                    profileService.updateProfile(
                        ProfileUpdateModel(
                            firstname = firstname, lastname = lastname,
                            patronymic = patronymic, birthday = birthday,
                            city = city, language = language,
                            email = email, phoneNumber = phoneNumber,
                            password = password
                        )
                    ).fold(
                        ifLeft = { exception ->
                            _state.update { it.copy(loading = false) }
                            ScreenNotification.push(exception)
                        },
                        ifRight = { profile ->
                            _state.update { ProfileState(currentProfile = profile) }
                            ScreenNotification.push(message = "Профиль обновлён")
                        }
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            profileService.logout()
        }
    }

}