package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber

@Serializable
@Resource("user")
class User()

@Serializable
@Resource("login")
class Login(
    val email: Email? = null,
    val phoneNumber: PhoneNumber? = null,
    val password: Password
)

@Serializable
@Resource("logout")
class Logout()