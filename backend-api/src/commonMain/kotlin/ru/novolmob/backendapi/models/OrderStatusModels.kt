package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.CreationTime
import ru.novolmob.core.models.Description
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.OrderStatusId

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
    val detail: OrderStatusDetailModel,
    val dateTime: CreationTime
)

@Serializable
data class OrderStatusShortModel(
    val id: OrderStatusId,
    val active: Boolean,
    val title: Title,
    val description: Description,
    val dateTime: CreationTime
)