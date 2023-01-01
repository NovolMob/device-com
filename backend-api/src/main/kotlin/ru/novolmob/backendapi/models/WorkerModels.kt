package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.WorkerId

@Serializable
data class WorkerInfoModel(
    val id: WorkerId,
    val pointId: PointId? = null,
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic? = null,
    val language: Language
)

@Serializable
data class WorkerModel(
    val id: WorkerId,
    val pointId: PointId? = null,
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic? = null,
    val language: Language,
    val phoneNumber: PhoneNumber,
    val email: Email?
)

@Serializable
data class WorkerCreateModel(
    val pointId: PointId? = null,
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic? = null,
    val language: Language,
    val password: Password,
    val phoneNumber: PhoneNumber,
    val email: Email? = null
)

@Serializable
data class WorkerUpdateModel(
    val pointId: PointId? = null,
    val firstname: Firstname? = null,
    val lastname: Lastname? = null,
    val patronymic: Patronymic? = null,
    val language: Language? = null,
    val password: Password? = null,
    val phoneNumber: PhoneNumber? = null,
    val email: Email? = null
)