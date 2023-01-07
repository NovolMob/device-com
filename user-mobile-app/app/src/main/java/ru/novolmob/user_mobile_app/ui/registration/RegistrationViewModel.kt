package ru.novolmob.user_mobile_app.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.computations.either
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.UserCreateModel
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.core.models.*
import ru.novolmob.user_mobile_app.mutablevalue.*
import ru.novolmob.user_mobile_app.services.ICityService
import ru.novolmob.user_mobile_app.utils.ScreenNotification
import ru.novolmob.user_mobile_app.services.IProfileService
import java.util.*

val RUSSIANS = Locale("ru", "Русский")

data class RegistrationState(
    val firstname: AbstractCharacterMutableValue<Firstname> = AbstractCharacterMutableValue.FirstnameMutableValue(),
    val lastname: AbstractCharacterMutableValue<Lastname> = AbstractCharacterMutableValue.LastnameMutableValue(),
    val patronymic: NullableAbstractCharacterMutableValue<Patronymic> = NullableAbstractCharacterMutableValue.PatronymicMutableValue(),
    val birthday: NullableAbstractLocalDateMutableValue<Birthday> = NullableAbstractLocalDateMutableValue.BirthdayMutableValue(),
    val city: CityMutableValue = CityMutableValue(),
    val availableLanguages: List<Locale> = AbstractCharacterMutableValue.LanguageMutableValue.availableLanguages,
    val language: AbstractCharacterMutableValue<Language> = AbstractCharacterMutableValue.LanguageMutableValue(),
    val email: NullableAbstractCharacterMutableValue<Email> = NullableAbstractCharacterMutableValue.EmailMutableValue(),
    val phoneNumber: AbstractCharacterMutableValue<PhoneNumber> = AbstractCharacterMutableValue.PhoneNumberMutableValue(),
    val password: PasswordMutableValue = PasswordMutableValue(),
    val canSend: Boolean = false,
    val loading: Boolean = false
)

class RegistrationViewModel(
    private val profileService: IProfileService,
    private val cityService: ICityService
): ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                cityService.cities.collectLatest { cities ->
                    state.value.city.cities.update { cities }
                }
            }
            launch {
                state.value.run {
                    combine(
                        firstname.valid, lastname.valid, patronymic.valid,
                        birthday.valid, city.valid, language.valid,
                        email.valid, phoneNumber.valid, password.valid
                    ) { array: Array<Boolean> ->
                        array.all { it }
                    }.collectLatest { canSend ->
                        _state.update { it.copy(canSend = canSend) }
                    }
                }
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            either<AbstractBackendException, UserModel> {
                _state.value.run {
                    profileService.register(
                        profileModel = UserCreateModel(
                            firstname = firstname.getModel().bind(),
                            lastname = lastname.getModel().bind(),
                            patronymic = patronymic.getModel().bind(),
                            birthday = birthday.getModel().bind(),
                            cityId = city.getModel().bind()?.id,
                            language = language.getModel().bind(),
                            email = email.getModel().bind(),
                            phoneNumber = phoneNumber.getModel().bind(),
                            password = password.getModel().bind()
                        )
                    ).bind()
                }
            }.fold(
                ifLeft = { exception ->
                    _state.update { it.copy(loading = false) }
                    ScreenNotification.push(exception)
                },
                ifRight = {  }
            )
        }
    }

}