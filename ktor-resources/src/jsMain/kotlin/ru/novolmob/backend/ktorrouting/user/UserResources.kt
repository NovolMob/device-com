package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("user")
actual class User

@Serializable
@Resource("login")
actual class Login

@Serializable
@Resource("logout")
actual class Logout

@Serializable
@Resource("registration")
actual class Registration