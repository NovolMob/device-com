package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.WorkerId

@Serializable
data class WorkerModel(
    val id: WorkerId,
    val pointId: PointId?,
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic,
    val language: Language
)

@Serializable
data class WorkerCreateModel(
    val pointId: PointId?,
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic,
    val language: Language,
    val password: Password,
    val phoneNumber: PhoneNumber,
    val email: Email?
)

@Serializable
data class WorkerUpdateModel(
    val pointId: PointId?,
    val firstname: Firstname?,
    val lastname: Lastname?,
    val patronymic: Patronymic?,
    val language: Language?,
    val password: Password?,
    val phoneNumber: PhoneNumber?,
    val email: Email?
)