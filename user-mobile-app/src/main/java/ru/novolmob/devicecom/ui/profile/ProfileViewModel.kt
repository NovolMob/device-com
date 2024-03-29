package ru.novolmob.devicecom.ui.profile

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.computations.either
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.CityShortModel
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.UserUpdateModel
import ru.novolmob.core.models.*
import ru.novolmob.devicecom.mutablevalue.*
import ru.novolmob.devicecom.services.ICityService
import ru.novolmob.devicecom.services.IProfileService
import ru.novolmob.devicecom.utils.ScreenNotification
import ru.novolmob.devicecom.utils.trim
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
    val availableLanguages: List<Locale> = Language.availableLanguages.map { (country, language) -> Locale(language.string, country) },
    val language: AbstractCharacterMutableValue<Language> =
        AbstractCharacterMutableValue.LanguageMutableValue(currentProfile?.language?.string ?: ""),
    val email: NullableAbstractCharacterMutableValue<Email> =
        NullableAbstractCharacterMutableValue.EmailMutableValue(currentProfile?.email?.string ?: ""),
    val phoneNumber: AbstractCharacterMutableValue<PhoneNumber> =
        AbstractCharacterMutableValue.PhoneNumberMutableValue(currentProfile?.phoneNumber?.trim() ?: ""),
    val password: PasswordMutableValue = PasswordMutableValue(),
    val canSave: Boolean = false,
    val loading: Boolean = false
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
                    state.value.run {
                        combine(
                            firstname.value, lastname.value, patronymic.value,
                            birthday.value, city.value, language.value,
                            email.value, phoneNumber.value, password.value
                        ) { array ->
                            (array[0] != userModel?.firstname?.string && firstname.isValid(array[0] as String)) ||
                                    (array[1] != userModel?.lastname?.string && lastname.isValid(array[1] as String)) ||
                                    (array[2] != userModel?.patronymic?.string && patronymic.isValid(array[2] as String)) ||
                                    (array[3] != userModel?.birthday?.date && birthday.isValid(array[3] as LocalDate?)) ||
                                    ((array[4] as CityShortModel?)?.id != userModel?.cityId && city.isValid(array[4] as CityShortModel?)) ||
                                    (array[5] != userModel?.language?.string && language.isValid(array[5] as String))||
                                    (array[6] != userModel?.email?.string && email.isValid(array[6] as String)) ||
                                    (array[7] != userModel?.phoneNumber?.trim() && phoneNumber.isValid(array[7] as String)) ||
                                    (array[8].toString().isNotEmpty() && password.isValid(array[8] as Password))
                        }.collectLatest { canSave ->
                            _state.update { it.copy(canSave = canSave) }
                        }
                    }
                }
            }
            launch {
                cityService.cities.collectLatest { cities ->
                    state.value.city.cities.update { cities }
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
                    ScreenNotification.push(exception)
                    _state.update { it.copy(loading = false) }
                },
                ifRight = { profile ->
                    setProfile(profile)
                    ScreenNotification.push(message = "Профиль обновлён")
                    _state.update { it.copy(loading = false) }
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