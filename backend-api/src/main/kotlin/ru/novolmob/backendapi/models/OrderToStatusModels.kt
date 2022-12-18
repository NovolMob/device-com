package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.OrderStatusId
import ru.novolmob.database.models.ids.OrderToStatusEntityId
import ru.novolmob.database.models.ids.WorkerId

@Serializable
data class OrderToStatusEntityModel(
    val id: OrderToStatusEntityId,
    val orderId: OrderId,
    val status: OrderStatusId,
    val workerId: WorkerId,
)

@Serializable
data class OrderToStatusEntityCreateModel(
    val orderId: OrderId,
    val status: OrderStatusId,
    val workerId: WorkerId,
)

@Serializable
data class OrderToStatusEntityUpdateModel(
    val orderId: OrderId?,
    val status: OrderStatusId?,
    val workerId: WorkerId?,
)