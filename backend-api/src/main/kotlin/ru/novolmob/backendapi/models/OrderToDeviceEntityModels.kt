package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.Amount
import ru.novolmob.database.models.Price
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.OrderToDeviceEntityId

@Serializable
data class OrderToDeviceEntityModel(
    val id: OrderToDeviceEntityId,
    val orderId: OrderId,
    val deviceId: DeviceId,
    val amount: Amount,
    val priceForOne: Price
)

@Serializable
data class OrderToDeviceEntityCreateModel(
    val orderId: OrderId,
    val deviceId: DeviceId,
    val amount: Amount,
    val priceForOne: Price
)

@Serializable
data class OrderToDeviceEntityUpdateModel(
    val orderId: OrderId?,
    val deviceId: DeviceId?,
    val amount: Amount?,
    val priceForOne: Price?
)