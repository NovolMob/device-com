package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.PointToDeviceEntityId

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
    val pointId: PointId? = null,
    val deviceId: DeviceId? = null,
    val amount: Amount? = null
)