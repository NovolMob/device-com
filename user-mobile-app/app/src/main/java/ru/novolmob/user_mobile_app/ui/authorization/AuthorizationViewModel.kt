package ru.novolmob.user_mobile_app.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.user_mobile_app.models.ScreenNotification
import ru.novolmob.user_mobile_app.services.IProfileService

data class AuthorizationState(
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val canEnter: Boolean = false,
    val loading: Boolean = false
)

class AuthorizationViewModel(
    private val profileService: IProfileService,
    private val emailRegex: Regex = Regex("\\w+@\\w+.\\w+"),
    private val phoneNumberRegex: Regex = Regex("\\+\\d+"),
): ViewModel() {
    private val _state = MutableStateFlow(AuthorizationState())
    val state = _state.asStateFlow()

    fun password(password: String) =
        _state.update {
            it.copy(password = password, canEnter = (validEmail(it.email) || validPhoneNumber(it.phoneNumber)) && password.isNotEmpty())
        }

    fun email(email: String)=
        _state.update {
            it.copy(email = email, canEnter = validEmail(email) && it.password.isNotEmpty())
        }

    fun phoneNumber(phoneNumber: String) =
        _state.update {
            it.copy(phoneNumber = phoneNumber, canEnter = validPhoneNumber(phoneNumber) && it.password.isNotEmpty())
        }

    fun reset() =
        _state.update { AuthorizationState() }

    fun login(): Unit = _state.value.let {
        if (it.password.isNotEmpty()) {
            if (validEmail(it.email)) {
                loginByEmail()
            } else if (validPhoneNumber(it.phoneNumber)) {
                loginByPhoneNumber()
            }
        }
    }

    fun loginByEmail() {
        if (_state.value.canEnter) {
            viewModelScope.launch {
                _state.update { it.copy(loading = true) }
                _state.value.run {
                    profileService.loginByEmail(
                        email = email,
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

    fun loginByPhoneNumber() {
        if (_state.value.canEnter) {
            viewModelScope.launch {
                _state.update { it.copy(loading = true) }
                _state.value.run {
                    profileService.loginByPhoneNumber(
                        phoneNumber = phoneNumber,
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

    fun validEmail(email: String): Boolean = emailRegex.matches(email)
    fun validPhoneNumber(phoneNumber: String): Boolean = phoneNumberRegex.matches(phoneNumber)
}