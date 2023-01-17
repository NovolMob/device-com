package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Address
import ru.novolmob.core.models.Schedule
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.core.models.ids.PointId

@Serializable
data class PointModel(
    val id: PointId,
    val cityId: CityId
)

@Serializable
class PointCreateModel(
    val cityId: CityId
)

@Serializable
class PointUpdateModel(
    val cityId: CityId? = null
)

@Serializable
data class PointFullModel(
    val id: PointId,
    val city: CityFullModel,
    val detail: PointDetailModel
)

@Serializable
data class PointShortModel(
    val id: PointId,
    val city: CityShortModel,
    val address: Address,
    val schedule: Schedule
)