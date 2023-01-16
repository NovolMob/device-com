package ru.novolmob.backend.ktorrouting.user

import kotlinx.serialization.Serializable

@Serializable
expect class User

@Serializable
expect class Login

@Serializable
expect class Logout

@Serializable
expect class Registration