package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.Amount
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.PointId
import ru.novolmob.database.models.ids.PointToDeviceEntityId

@Serializable
data class PointToDeviceEntityModel(
    val id: PointToDeviceEntityId,
    val pointId: PointId,
    val deviceId: DeviceId,
    val amount: Amount
)

@Serializable
data class PointToDeviceEntityCreateModel(
    val pointId: PointId,
    val deviceId: DeviceId,
    val amount: Amount
)

@Serializable
data class PointToDeviceEntityUpdateModel(
    val pointId: PointId?,
    val deviceId: DeviceId?,
    val amount: Amount?
)