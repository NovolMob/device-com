package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.Amount
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.PointId

@Serializable
data class PointModel(
    val id: PointId
)

@Serializable
class PointCreateModel()

@Serializable
class PointUpdateModel()

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