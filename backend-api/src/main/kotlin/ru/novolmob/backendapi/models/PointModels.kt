package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.ids.PointId

@Serializable
data class PointModel(
    val id: PointId
)

@Serializable
class PointCreateModel()

@Serializable
class PointUpdateModel()