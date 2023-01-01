package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("user")
class User

@Serializable
@Resource("login")
class Login

@Serializable
@Resource("logout")
class Logout

@Serializable
@Resource("registration")
class Registration