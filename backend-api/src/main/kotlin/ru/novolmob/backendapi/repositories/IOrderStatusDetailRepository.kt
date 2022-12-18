package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderStatusDetailCreateModel
import ru.novolmob.backendapi.models.OrderStatusDetailModel
import ru.novolmob.backendapi.models.OrderStatusDetailUpdateModel
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderStatusDetailId
import ru.novolmob.core.models.ids.OrderStatusId

interface IOrderStatusDetailRepository: ICrudRepository<OrderStatusDetailId, OrderStatusDetailModel, OrderStatusDetailCreateModel, OrderStatusDetailUpdateModel> {
    suspend fun getDetailFor(orderStatusId: OrderStatusId, language: Language): Either<BackendException, OrderStatusDetailModel>
    suspend fun removeDetailFor(orderStatusId: OrderStatusId): Either<BackendException, Boolean>
}