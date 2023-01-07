package ru.novolmob.user_mobile_app.ui.profile

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.computations.either
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.CityShortModel
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.UserUpdateModel
import ru.novolmob.core.models.*
import ru.novolmob.user_mobile_app.mutablevalue.*
import ru.novolmob.user_mobile_app.services.ICityService
import ru.novolmob.user_mobile_app.services.IProfileService
import ru.novolmob.user_mobile_app.utils.ScreenNotification
import ru.novolmob.user_mobile_app.utils.trim
import java.util.*

data class ProfileState(
    val avatar: ImageBitmap? = null,
    val currentProfile: UserModel?,
    val firstname: AbstractCharacterMutableValue<Firstname> =
        AbstractCharacterMutableValue.FirstnameMutableValue(currentProfile?.firstname?.string ?: ""),
    val lastname: AbstractCharacterMutableValue<Lastname> =
        AbstractCharacterMutableValue.LastnameMutableValue(currentProfile?.lastname?.string ?: ""),
    val patronymic: NullableAbstractCharacterMutableValue<Patronymic> =
        NullableAbstractCharacterMutableValue.PatronymicMutableValue(currentProfile?.patronymic?.string ?: ""),
    val birthday: NullableAbstractLocalDateMutableValue<Birthday> =
        NullableAbstractLocalDateMutableValue.BirthdayMutableValue(currentProfile?.birthday?.date),
    val city: CityMutableValue,
    val availableLanguages: List<Locale> = AbstractCharacterMutableValue.LanguageMutableValue.availableLanguages,
    val language: AbstractCharacterMutableValue<Language> =
        AbstractCharacterMutableValue.LanguageMutableValue(currentProfile?.language?.string ?: ""),
    val email: NullableAbstractCharacterMutableValue<Email> =
        NullableAbstractCharacterMutableValue.EmailMutableValue(currentProfile?.email?.string ?: ""),
    val phoneNumber: AbstractCharacterMutableValue<PhoneNumber> =
        AbstractCharacterMutableValue.PhoneNumberMutableValue(currentProfile?.phoneNumber?.trim() ?: ""),
    val password: PasswordMutableValue = PasswordMutableValue(),
    val canSave: Boolean = false,
    val loading: Boolean = false,
    val logout: Boolean = false
) {
    val initials = currentProfile?.let { "${it.firstname.string.first { it.isLetter() }}${it.lastname.string.first { it.isLetter() }}" } ?: ""
}

class ProfileViewModel(
    private val profileService: IProfileService,
    private val cityService: ICityService
): ViewModel() {
    private val _state = MutableStateFlow(
        profileService.get().let {
            ProfileState(
                currentProfile = it,
                city = CityMutableValue(cityService.cities.value, it?.cityId)
            )
        }
    )
    val state = _state.asStateFlow()

    private fun setProfile(userModel: UserModel?) {
        _state.update {
            it.firstname.set(userModel?.firstname?.string ?: "")
            it.lastname.set(userModel?.lastname?.string ?: "")
            it.patronymic.set(userModel?.patronymic?.string ?: "")
            it.birthday.set(userModel?.birthday?.date)
            it.city.set(userModel?.cityId)
            it.language.set(userModel?.language?.string ?: "")
            it.email.set(userModel?.email?.string ?: "")
            it.phoneNumber.set(userModel?.phoneNumber?.trim() ?: "")
            it.copy(
                currentProfile = userModel
            )
        }
    }

    init {
        viewModelScope.launch {
            launch {
                profileService.profile.collectLatest { userModel ->
                    setProfile(userModel)
                }
            }
            launch {
                cityService.cities.collectLatest { cities ->
                    state.value.city.cities.update { cities }
                }
            }
            launch {
                state.value.run {
                    combine(
                        firstname.value, lastname.value, patronymic.value,
                        birthday.value, city.value, language.value,
                        email.value, phoneNumber.value, password.value
                    ) { array ->
                        (array[0] != currentProfile?.firstname?.string && firstname.valid.value) ||
                                (array[1] != currentProfile?.lastname?.string && lastname.valid.value) ||
                                (array[2] != currentProfile?.patronymic?.string && patronymic.valid.value) ||
                                (array[3] != currentProfile?.birthday?.date && birthday.valid.value) ||
                                ((array[4] as CityShortModel?)?.id != currentProfile?.cityId && city.valid.value) ||
                                (array[5] != currentProfile?.language?.string && language.valid.value)||
                                (array[6] != currentProfile?.email?.string && email.valid.value) ||
                                (array[7] != currentProfile?.phoneNumber?.trim() && phoneNumber.valid.value) ||
                                (array[8].toString().isNotEmpty() && password.valid.value)
                    }.collectLatest { canSave ->
                        _state.update { it.copy(canSave = canSave) }
                    }
                }
            }
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            either<AbstractBackendException, UserModel> {
                _state.value.run {
                    profileService.updateProfile(
                        UserUpdateModel(
                            firstname = if (firstname.value.value != currentProfile?.firstname?.string) firstname.getModel().bind() else null,
                            lastname = if (lastname.value.value != currentProfile?.lastname?.string) lastname.getModel().bind() else null,
                            patronymic = if (patronymic.value.value != currentProfile?.patronymic?.string) patronymic.getModel().bind() else null,
                            birthday = if (birthday.value.value != currentProfile?.birthday?.date) birthday.getModel().bind() else null,
                            cityId = if (city.value.value?.id != currentProfile?.cityId) city.getModel().bind()?.id else null,
                            language = if (language.value.value != currentProfile?.language?.string) language.getModel().bind() else null,
                            email = if (email.value.value != currentProfile?.email?.string) email.getModel().bind() else null,
                            phoneNumber = if (phoneNumber.value.value != currentProfile?.phoneNumber?.trim()) phoneNumber.getModel().bind() else null,
                            password = if (password.value.value.string.isNotEmpty()) password.getModel().bind() else null
                        )
                    ).bind()
                }
            }.fold(
                ifLeft = { exception ->
                    _state.update { it.copy(loading = false) }
                    ScreenNotification.push(exception)
                },
                ifRight = { profile ->
                    setProfile(profile)
                    ScreenNotification.push(message = "Профиль обновлён")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            profileService.logout()
        }
    }

}