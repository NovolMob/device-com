package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.Address
import ru.novolmob.database.models.Description
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.Schedule
import ru.novolmob.database.models.ids.PointDetailId
import ru.novolmob.database.models.ids.PointId

@Serializable
data class PointDetailModel(
    val id: PointDetailId,
    val pointId: PointId,
    val address: Address,
    val schedule: Schedule,
    val description: Description,
    val language: Language
)

@Serializable
data class PointDetailCreateModel(
    val pointId: PointId,
    val address: Address,
    val schedule: Schedule,
    val description: Description,
    val language: Language
)

@Serializable
data class PointDetailUpdateModel(
    val pointId: PointId?,
    val address: Address?,
    val schedule: Schedule?,
    val description: Description?,
    val language: Language?
)