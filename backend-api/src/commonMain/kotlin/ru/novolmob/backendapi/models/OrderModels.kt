package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.*
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
    val point: Address,
    val list: List<OrderItemShortModel>,
    val totalCost: Price,
    val status: Title?,
    val active: Boolean,
    val creationTime: CreationTime
)

@Serializable
data class OrderItemShortModel(
    val deviceId: DeviceId,
    val title: Title,
    val amount: Amount,
    val priceForOne: Price
)

@Serializable
data class OrderFullModel(
    val id: OrderId,
    val userId: UserId,
    val point: PointFullModel,
    val list: List<OrderItemModel>,
    val totalCost: Price,
    val lastStatus: OrderStatusShortModel?,
    val creationTime: CreationTime
)

@Serializable
data class OrderItemModel(
    val deviceId: DeviceId,
    val title: Title,
    val description: Description,
    val amount: Amount,
    val priceForOne: Price
)