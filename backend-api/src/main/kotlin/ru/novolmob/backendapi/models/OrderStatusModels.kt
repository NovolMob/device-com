package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.ids.OrderStatusId

@Serializable
data class OrderStatusModel(
    val id: OrderStatusId,
    val active: Boolean
)

@Serializable
data class OrderStatusCreateModel(
    val active: Boolean
)

@Serializable
data class OrderStatusUpdateModel(
    val active: Boolean?
)

@Serializable
data class OrderStatusFullModel(
    val id: OrderStatusId,
    val active: Boolean,
    val detail: OrderStatusDetailModel
)