package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Description
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.OrderStatusDetailId
import ru.novolmob.core.models.ids.OrderStatusId

@Serializable
data class OrderStatusDetailModel(
    val id: OrderStatusDetailId,
    val orderStatusId: OrderStatusId,
    val title: Title,
    val description: Description,
    val language: Language
)

@Serializable
data class OrderStatusDetailCreateModel(
    val orderStatusId: OrderStatusId,
    val title: Title,
    val description: Description,
    val language: Language
)

@Serializable
data class OrderStatusDetailUpdateModel(
    val orderStatusId: OrderStatusId?,
    val title: Title?,
    val description: Description?,
    val language: Language?
)