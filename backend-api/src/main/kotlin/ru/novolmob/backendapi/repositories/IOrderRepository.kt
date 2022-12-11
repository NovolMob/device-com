package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderCreateModel
import ru.novolmob.backendapi.models.OrderFullModel
import ru.novolmob.backendapi.models.OrderModel
import ru.novolmob.backendapi.models.OrderUpdateModel
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.UserId

interface IOrderRepository: ICrudRepository<OrderId, OrderModel, OrderCreateModel, OrderUpdateModel> {
    suspend fun getFull(orderId: OrderId): Either<BackendException, OrderFullModel>
    suspend fun getOrdersFor(userId: UserId): Either<BackendException, List<OrderModel>>
}