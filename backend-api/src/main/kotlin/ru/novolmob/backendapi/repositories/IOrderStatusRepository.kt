package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderStatusCreateModel
import ru.novolmob.backendapi.models.OrderStatusFullModel
import ru.novolmob.backendapi.models.OrderStatusModel
import ru.novolmob.backendapi.models.OrderStatusUpdateModel
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.OrderStatusId

interface IOrderStatusRepository: ICrudRepository<OrderStatusId, OrderStatusModel, OrderStatusCreateModel, OrderStatusUpdateModel> {
    suspend fun getFull(orderStatusId: OrderStatusId, language: Language): Either<BackendException, OrderStatusFullModel>
    suspend fun getOrderStatus(orderId: OrderId): Either<BackendException, OrderStatusFullModel>
}