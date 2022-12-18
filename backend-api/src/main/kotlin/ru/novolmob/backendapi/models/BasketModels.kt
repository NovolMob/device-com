package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.ids.BasketId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.UserId

@Serializable
data class BasketModel(
    val id: BasketId,
    val userId: UserId,
    val deviceId: DeviceId,
    val amount: Amount
)

@Serializable
data class BasketCreateModel(
    val userId: UserId,
    val deviceId: DeviceId,
    val amount: Amount
)

@Serializable
data class BasketUpdateModel(
    val userId: UserId?,
    val deviceId: DeviceId?,
    val amount: Amount?
)

@Serializable
data class BasketItemModel(
    val deviceId: DeviceId,
    val amount: Amount
)
