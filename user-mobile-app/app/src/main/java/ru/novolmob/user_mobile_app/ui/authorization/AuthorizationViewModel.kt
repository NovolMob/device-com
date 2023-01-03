package ru.novolmob.user_mobile_app.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.computations.either
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.user_mobile_app.mutablevalue.AbstractCharacterMutableValue
import ru.novolmob.user_mobile_app.services.IProfileService
import ru.novolmob.user_mobile_app.utils.ScreenNotification

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
            _state.collectLatest {
                it.run {
                    combine(email.valid, phoneNumber.valid, password.valid) { array: Array<Boolean> ->
                        (array[0] || array[1]) && array[2]
                    }.collectLatest { canEnter ->
                        _state.update { it.copy(canEnter = canEnter) }
                    }
                }
            }
        }
    }

    fun reset() =
        _state.update { AuthorizationState() }

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
                either<BackendException, UserModel> {
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
                either<BackendException, UserModel> {
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