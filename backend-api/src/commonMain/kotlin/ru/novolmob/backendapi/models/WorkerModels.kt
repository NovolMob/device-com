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
    override val password: Password,
    override val phoneNumber: PhoneNumber,
    override val email: Email? = null
): WorkerCredentialUpdate

@Serializable
data class WorkerUpdateModel(
    val pointId: PointId? = null,
    val firstname: Firstname? = null,
    val lastname: Lastname? = null,
    val patronymic: Patronymic? = null,
    val language: Language? = null,
    override val password: Password? = null,
    override val phoneNumber: PhoneNumber? = null,
    override val email: Email? = null
): WorkerCredentialUpdate