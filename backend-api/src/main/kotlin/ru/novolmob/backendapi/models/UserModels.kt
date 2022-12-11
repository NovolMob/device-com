package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.*
import ru.novolmob.database.models.ids.UserId

@Serializable
data class UserModel(
    val id: UserId,
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic?,
    val birthday: Birthday?,
    val city: City?,
    val language: Language
)

@Serializable
data class UserCreateModel(
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic?,
    val birthday: Birthday?,
    val city: City?,
    val language: Language,
    val password: Password,
    val phoneNumber: PhoneNumber,
    val email: Email?
)

@Serializable
data class UserUpdateModel(
    val firstname: Firstname?,
    val lastname: Lastname?,
    val patronymic: Patronymic?,
    val birthday: Birthday?,
    val city: City?,
    val language: Language?,
    val password: Password?,
    val phoneNumber: PhoneNumber?,
    val email: Email?
)