package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.CreationDate
import ru.novolmob.database.models.UpdateDate
import ru.novolmob.database.models.ids.PointId

@Serializable
data class PointModel(
    val id: PointId,
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)

@Serializable
data class PointCreateModel(
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)

@Serializable
data class PointUpdateModel(
    val updateDate: UpdateDate?,
    val creationDate: CreationDate?
)