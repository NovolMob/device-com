package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeId

@Serializable
data class DeviceModel(
    val id: DeviceId,
    val article: Code,
    val typeId: DeviceTypeId,
    val amount: Amount,
    val price: Price
)

@Serializable
data class DeviceCreateModel(
    val article: Code,
    val typeId: DeviceTypeId,
    val amount: Amount,
    val price: Price
)

@Serializable
data class DeviceUpdateModel(
    val article: Code? = null,
    val typeId: DeviceTypeId? = null,
    val amount: Amount? = null,
    val price: Price? = null
)

@Serializable
data class DeviceFullModel(
    val id: DeviceId,
    val article: Code,
    val type: DeviceTypeFullModel,
    val detailModel: DeviceDetailModel,
    val amount: Amount,
    val price: Price
)

@Serializable
data class DeviceShortModel(
    val id: DeviceId,
    val title: Title,
    val description: Description,
    val amount: Amount,
    val price: Price
)