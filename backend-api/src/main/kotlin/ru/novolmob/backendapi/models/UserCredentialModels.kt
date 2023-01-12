package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.models.ids.UserId

@Serializable
data class UserCredentialModel(
    val userId: UserId,
    val phoneNumber: PhoneNumber,
    val email: Email? = null,
    val password: Password
)

interface UserCredentialUpdate {
    val phoneNumber: PhoneNumber?
    val email: Email?
    val password: Password?
}