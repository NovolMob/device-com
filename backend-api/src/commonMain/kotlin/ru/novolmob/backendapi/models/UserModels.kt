package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.core.models.ids.UserId

@Serializable
data class UserInfoModel(
    val id: UserId,
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic? = null,
    val birthday: Birthday? = null,
    val cityId: CityId? = null,
    val language: Language,
)

@Serializable
data class UserModel(
    val id: UserId,
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic? = null,
    val birthday: Birthday? = null,
    val cityId: CityId? = null,
    val language: Language,
    val phoneNumber: PhoneNumber,
    val email: Email? = null
)

@Serializable
data class UserCreateModel(
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic? = null,
    val birthday: Birthday? = null,
    val cityId: CityId? = null,
    val language: Language,
    override val password: Password,
    override val phoneNumber: PhoneNumber,
    override val email: Email? = null
): UserCredentialUpdate

@Serializable
data class UserUpdateModel(
    val firstname: Firstname? = null,
    val lastname: Lastname? = null,
    val patronymic: Patronymic? = null,
    val birthday: Birthday? = null,
    val cityId: CityId? = null,
    val language: Language? = null,
    override val password: Password? = null,
    override val phoneNumber: PhoneNumber? = null,
    override val email: Email? = null
): UserCredentialUpdate