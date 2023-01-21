package ru.novolmob.devicecom.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.computations.either
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.devicecom.mutablevalue.AbstractCharacterMutableValue
import ru.novolmob.devicecom.services.IProfileService
import ru.novolmob.devicecom.utils.ScreenNotification

data class AuthorizationState(
    val email: AbstractCharacterMutableValue<Email> = AbstractCharacterMutableValue.EmailMutableValue(),
    val phoneNumber: AbstractCharacterMutableValue<PhoneNumber> = AbstractCharacterMutableValue.PhoneNumberMutableValue(),
    val password: AbstractCharacterMutableValue<Password> = AbstractCharacterMutableValue.PasswordMutableValue(),
    val canEnter: Boolean = false,
    val loading: Boolean = false
)

class AuthorizationViewModel(
    private val profileService: IProfileService,
): ViewModel() {
    private val _state = MutableStateFlow(AuthorizationState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                profileService.action.collectLatest { action ->
                    _state.update {
                        it.copy(
                            loading = action == null,
                            canEnter = action == null
                        )
                    }
                }
            }
            launch {
                state.value.run {
                    combine(email.valid, phoneNumber.valid, password.valid) { array: Array<Boolean> ->
                        (array[0] || array[1]) && array[2]
                    }.collectLatest { canEnter ->
                        _state.update { it.copy(canEnter = canEnter) }
                    }
                }
            }
        }
    }

    fun reset() {
        state.value.run {
            email.clear()
            phoneNumber.clear()
            password.clear()
        }
    }

    fun login(): Unit = _state.value.run {
        if (password.valid.value) {
            if (email.valid.value) {
                loginByEmail()
            } else if (phoneNumber.valid.value) {
                loginByPhoneNumber()
            }
        }
    }

    fun loginByEmail() {
        if (_state.value.canEnter) {
            viewModelScope.launch {
                _state.update { it.copy(loading = true) }
                either<AbstractBackendException, UserModel> {
                    _state.value.run {
                        profileService.loginByEmail(
                            email = email.getModel().bind(),
                            password = password.getModel().bind()
                        ).bind()
                    }
                }.fold(
                    ifLeft = { exception ->
                        _state.update { it.copy(loading = false) }
                        ScreenNotification.push(exception)
                    },
                    ifRight = { reset() }
                )
            }
        }
    }

    fun loginByPhoneNumber() {
        if (_state.value.canEnter) {
            viewModelScope.launch {
                _state.update { it.copy(loading = true) }
                either<AbstractBackendException, UserModel> {
                    _state.value.run {
                        profileService.loginByPhoneNumber(
                            phoneNumber = phoneNumber.getModel().bind(),
                            password = password.getModel().bind()
                        ).bind()
                    }
                }.fold(
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