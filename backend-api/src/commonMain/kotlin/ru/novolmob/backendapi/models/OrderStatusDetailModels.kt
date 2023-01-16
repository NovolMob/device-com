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
    val parentId: OrderStatusId,
    val title: Title,
    val description: Description,
    val language: Language
)

@Serializable
data class OrderStatusDetailCreateModel(
    override val parentId: OrderStatusId,
    val title: Title,
    val description: Description,
    override val language: Language
): DetailCreate<OrderStatusId>

@Serializable
data class OrderStatusDetailUpdateModel(
    override val parentId: OrderStatusId? = null,
    val title: Title? = null,
    val description: Description? = null,
    override val language: Language? = null
): DetailUpdate<OrderStatusId>