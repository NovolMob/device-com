package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.UserId

interface IOrderRepository: ICrudRepository<OrderId, OrderModel, OrderCreateModel, OrderUpdateModel> {
    suspend fun getFull(orderId: OrderId, language: Language): Either<BackendException, OrderFullModel>
    suspend fun getOrdersFor(userId: UserId, language: Language): Either<BackendException, List<OrderShortModel>>
}