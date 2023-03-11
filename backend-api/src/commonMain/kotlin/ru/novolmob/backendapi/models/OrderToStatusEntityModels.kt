package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.core.models.ids.OrderToStatusEntityId
import ru.novolmob.core.models.ids.WorkerId

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
    val orderId: OrderId? = null,
    val status: OrderStatusId? = null,
    val workerId: WorkerId? = null,
)