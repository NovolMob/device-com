package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.UserId

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
    val userId: UserId? = null,
    val pointId: PointId? = null,
    val totalCost: Price? = null
)

@Serializable
data class OrderShortModel(
    val id: OrderId,
    val userId: UserId,
    val point: PointShortModel,
    val list: List<OrderItemModel>,
    val totalCost: Price,
    val lastStatus: OrderStatusShortModel
)

@Serializable
data class OrderFullModel(
    val id: OrderId,
    val userId: UserId,
    val point: PointFullModel,
    val list: List<OrderItemModel>,
    val totalCost: Price,
    val statuses: List<OrderStatusFullModel>
)

@Serializable
data class OrderItemModel(
    val deviceId: DeviceId,
    val amount: Amount,
    val priceForOne: Price
)