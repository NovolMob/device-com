package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber

@Serializable
data class LoginModel(
    val email: Email? = null,
    val phoneNumber: PhoneNumber? = null,
    val password: Password
)
