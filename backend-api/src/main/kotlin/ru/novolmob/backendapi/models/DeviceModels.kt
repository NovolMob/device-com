package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.Code
import ru.novolmob.database.models.Price
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.DeviceTypeId

@Serializable
data class DeviceModel(
    val id: DeviceId,
    val article: Code,
    val typeId: DeviceTypeId,
    val price: Price
)

@Serializable
data class DeviceCreateModel(
    val article: Code,
    val typeId: DeviceTypeId,
    val price: Price
)

@Serializable
data class DeviceUpdateModel(
    val article: Code?,
    val typeId: DeviceTypeId?,
    val price: Price?
)

@Serializable
data class DeviceFullModel(
    val id: DeviceId,
    val article: Code,
    val type: DeviceTypeFullModel,
    val detailModel: DeviceDetailModel,
    val points: List<NumberOfDeviceInPointModel>,
    val price: Price
)