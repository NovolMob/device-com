package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.*
import ru.novolmob.database.models.ids.PointId
import ru.novolmob.database.models.ids.WorkerId

@Serializable
data class WorkerModel(
    val id: WorkerId,
    val pointId: PointId,
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic,
    val language: Language,
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)

@Serializable
data class WorkerCreateModel(
    val pointId: PointId,
    val firstname: Firstname,
    val lastname: Lastname,
    val patronymic: Patronymic,
    val language: Language,
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)

@Serializable
data class WorkerUpdateModel(
    val pointId: PointId?,
    val firstname: Firstname?,
    val lastname: Lastname?,
    val patronymic: Patronymic?,
    val language: Language?,
    val updateDate: UpdateDate?,
    val creationDate: CreationDate?
)