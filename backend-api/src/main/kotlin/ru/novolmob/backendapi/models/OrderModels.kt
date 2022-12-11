package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.Amount
import ru.novolmob.database.models.Price
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.PointId
import ru.novolmob.database.models.ids.UserId

@Serializable
data class OrderModel(
    val id: OrderId,
    val userId: UserId,
    val pointId: PointId,
    val totalCost: Price
)

@Serializable
data class OrderCreateModel(
    val userId: UserId,
    val pointId: PointId,
    val totalCost: Price
)

@Serializable
data class OrderUpdateModel(
    val userId: UserId?,
    val pointId: PointId?,
    val totalCost: Price?
)

@Serializable
data class OrderFullModel(
    val id: OrderId,
    val userId: UserId,
    val point: PointFullModel,
    val list: List<OrderItemModel>,
    val totalCost: Price,
    val status: OrderStatusFullModel
)

@Serializable
data class OrderItemModel(
    val deviceId: DeviceId,
    val amount: Amount,
    val priceForOne: Price
)