package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderStatusDetailCreateModel
import ru.novolmob.backendapi.models.OrderStatusDetailModel
import ru.novolmob.backendapi.models.OrderStatusDetailUpdateModel
import ru.novolmob.database.models.ids.OrderStatusDetailId
import ru.novolmob.database.models.ids.OrderStatusId

interface IOrderStatusDetailRepository: ICrudRepository<OrderStatusDetailId, OrderStatusDetailModel, OrderStatusDetailCreateModel, OrderStatusDetailUpdateModel> {
    suspend fun getDetailFor(orderStatusId: OrderStatusId): Either<BackendException, OrderStatusDetailModel>
    suspend fun removeDetailFor(orderStatusId: OrderStatusId): Either<BackendException, Boolean>
}