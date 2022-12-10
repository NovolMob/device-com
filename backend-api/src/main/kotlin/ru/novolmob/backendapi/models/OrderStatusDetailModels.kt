package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.*
import ru.novolmob.database.models.ids.OrderStatusDetailId
import ru.novolmob.database.models.ids.OrderStatusId

@Serializable
data class OrderStatusDetailModel(
    val id: OrderStatusDetailId,
    val orderStatusId: OrderStatusId,
    val title: Title,
    val description: Description,
    val language: Language,
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)

@Serializable
data class OrderStatusDetailCreateModel(
    val orderStatusId: OrderStatusId,
    val title: Title,
    val description: Description,
    val language: Language,
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)

@Serializable
data class OrderStatusDetailUpdateModel(
    val orderStatusId: OrderStatusId?,
    val title: Title?,
    val description: Description?,
    val language: Language?,
    val updateDate: UpdateDate?,
    val creationDate: CreationDate?
)