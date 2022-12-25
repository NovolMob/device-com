package ru.novolmob.user_mobile_app.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.user_mobile_app.models.ProfileModel
import ru.novolmob.user_mobile_app.models.ScreenNotification
import ru.novolmob.user_mobile_app.services.IProfileService
import java.time.LocalDate
import java.util.*

val RUSSIANS = Locale("ru", "Русский")

data class RegistrationState(
    val firstname: String = "",
    val lastname: String = "",
    val patronymic: String = "",
    val birthday: LocalDate? = null,
    val availableCities: List<String> = listOf(
        "Великий Новгород"
    ),
    val city: String? = null,
    val availableLanguages: List<Locale> = listOf(
        RUSSIANS
    ),
    val language: Locale = RUSSIANS,
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val canSend: Boolean = false,
    val loading: Boolean = false
)

class RegistrationViewModel(
    private val profileService: IProfileService,
    private val emailRegex: Regex = Regex("\\w+@\\w+.\\w+"),
    private val phoneNumberRegex: Regex = Regex("\\+\\d+"),
): ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    fun reset() = _state.update { RegistrationState() }

    fun firstname(firstname: String) =
        _state.update {
            it.copy(
                firstname = firstname,
                canSend = firstname.isNotEmpty()
                        && it.lastname.isNotEmpty()
                        && (it.birthday == null || validBirthday(it.birthday))
                        && validPhoneNumber(it.phoneNumber)
                        && (it.email.isEmpty() || validEmail(it.email))
                        && it.password.isNotEmpty()
            )
        }

    fun lastname(lastname: String) =
        _state.update {
            it.copy(
                lastname = lastname,
                canSend = it.firstname.isNotEmpty()
                        && lastname.isNotEmpty()
                        && (it.birthday == null || validBirthday(it.birthday))
                        && validPhoneNumber(it.phoneNumber)
                        && (it.email.isEmpty() || validEmail(it.email))
                        && it.password.isNotEmpty()
            )
        }

    fun patronymic(patronymic: String) =
        _state.update {
            it.copy(patronymic = patronymic)
        }

    fun birthday(birthday: LocalDate?) =
        _state.update {
            it.copy(
                birthday = birthday,
                canSend = it.firstname.isNotEmpty()
                        && it.lastname.isNotEmpty()
                        && (birthday == null || validBirthday(birthday))
                        && validPhoneNumber(it.phoneNumber)
                        && (it.email.isEmpty() || validEmail(it.email))
                        && it.password.isNotEmpty()
            )
        }

    fun city(city: String?) =
        _state.update {
            it.copy(city = city)
        }

    fun language(language: Locale) =
        _state.update {
            it.copy(language = language)
        }

    fun email(email: String) =
        _state.update {
            it.copy(
                email = email,
                canSend = it.firstname.isNotEmpty()
                        && it.lastname.isNotEmpty()
                        && (it.birthday == null || validBirthday(it.birthday))
                        && validPhoneNumber(it.phoneNumber)
                        && (email.isEmpty() || validEmail(email))
                        && it.password.isNotEmpty()
            )
        }

    fun phoneNumber(phoneNumber: String) =
        _state.update {
            it.copy(
                phoneNumber = phoneNumber,
                canSend = it.firstname.isNotEmpty()
                        && it.lastname.isNotEmpty()
                        && (it.birthday == null || validBirthday(it.birthday))
                        && validPhoneNumber(phoneNumber)
                        && (it.email.isEmpty() || validEmail(it.email))
                        && it.password.isNotEmpty()
            )
        }

    fun password(password: String) =
        _state.update {
            it.copy(
                password = password,
                canSend = it.firstname.isNotEmpty()
                        && it.lastname.isNotEmpty()
                        && (it.birthday == null || validBirthday(it.birthday))
                        && validPhoneNumber(it.phoneNumber)
                        && (it.email.isEmpty() || validEmail(it.email))
                        && password.isNotEmpty()
            )
        }

    fun validEmail(email: String): Boolean = emailRegex.matches(email)
    fun validBirthday(birthday: LocalDate): Boolean = birthday < LocalDate.now()
    fun validPhoneNumber(phoneNumber: String): Boolean = phoneNumberRegex.matches(phoneNumber)

    fun registration() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            _state.value.run {
                profileService.register(
                    profileModel = ProfileModel(
                        firstname = firstname, lastname = lastname,
                        patronymic = patronymic, birthday = birthday,
                        city = city, language = language,
                        email = email, phoneNumber = phoneNumber
                    ),
                    password = password
                ).fold(
                    ifLeft = { exception ->
                        _state.update { it.copy(loading = false) }
                        ScreenNotification.push(exception)
                    },
                    ifRight = { reset() }
                )
            }
        }
    }

}