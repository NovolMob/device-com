package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Address
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.City
import ru.novolmob.core.models.Schedule
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.PointId

@Serializable
data class PointModel(
    val id: PointId,
    val city: City
)

@Serializable
class PointCreateModel(
    val city: City
)

@Serializable
class PointUpdateModel(
    val city: City? = null
)

@Serializable
data class NumberOfDeviceInPointModel(
    val pointId: PointId,
    val pointDetail: PointDetailModel,
    val amount: Amount
)

@Serializable
data class PointFullModel(
    val id: PointId,
    val detail: PointDetailModel
)

@Serializable
data class PointItemModel(
    val deviceId: DeviceId,
    val amount: Amount
)

@Serializable
data class PointShortModel(
    val id: PointId,
    val address: Address,
    val schedule: Schedule
)