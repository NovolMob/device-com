package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.UserId

interface IOrderRepository: IRepository {
    suspend fun confirmOrder(userId: UserId, pointId: PointId, language: Language): Either<AbstractBackendException, OrderShortModel>
    suspend fun cancelOrder(orderId: OrderId): Either<AbstractBackendException, Boolean>
    suspend fun getFull(orderId: OrderId, language: Language): Either<AbstractBackendException, OrderFullModel>
    suspend fun getOrdersFor(userId: UserId, language: Language): Either<AbstractBackendException, List<OrderShortModel>>
}