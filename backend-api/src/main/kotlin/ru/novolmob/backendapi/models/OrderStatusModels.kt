package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.OrderStatusId
import ru.novolmob.database.models.ids.WorkerId

@Serializable
data class OrderStatusModel(
    val id: OrderStatusId,
    val orderId: OrderId,
    val workerId: WorkerId
)

@Serializable
data class OrderStatusCreateModel(
    val orderId: OrderId,
    val workerId: WorkerId
)

@Serializable
data class OrderStatusUpdateModel(
    val orderId: OrderId?,
    val workerId: WorkerId?
)

@Serializable
data class OrderStatusFullModel(
    val id: OrderStatusId,
    val orderId: OrderId,
    val workerId: WorkerId,
    val detail: OrderStatusDetailModel
)