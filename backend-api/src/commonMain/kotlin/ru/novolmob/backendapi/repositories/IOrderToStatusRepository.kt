package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.OrderStatusFullModel
import ru.novolmob.backendapi.models.OrderStatusShortModel
import ru.novolmob.backendapi.models.OrderToStatusEntityCreateModel
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.OrderStatusId

interface IOrderToStatusRepository: IRepository {
    suspend fun getStatuses(orderId: OrderId, language: Language): Either<AbstractBackendException, List<OrderStatusFullModel>>
    suspend fun getLastStatus(orderId: OrderId, language: Language): Either<AbstractBackendException, OrderStatusShortModel>
    suspend fun post(createModel: OrderToStatusEntityCreateModel): Either<AbstractBackendException, Unit>
    suspend fun delete(orderId: OrderId, orderStatusId: OrderStatusId): Either<AbstractBackendException, Boolean>
}