package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.OrderToStatusEntityId

interface IOrderToStatusRepository: ICrudRepository<OrderToStatusEntityId, OrderToStatusEntityModel, OrderToStatusEntityCreateModel, OrderToStatusEntityUpdateModel> {
    suspend fun getStatuses(orderId: OrderId, language: Language): Either<AbstractBackendException, List<OrderStatusFullModel>>
    suspend fun getLastStatus(orderId: OrderId, language: Language): Either<AbstractBackendException, OrderStatusShortModel>
}