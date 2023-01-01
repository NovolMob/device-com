package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Code
import ru.novolmob.core.models.Description
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeId

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
    val article: Code? = null,
    val typeId: DeviceTypeId? = null,
    val price: Price? = null
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

@Serializable
data class DeviceShortModel(
    val id: DeviceId,
    val title: Title,
    val description: Description,
    val price: Price
)