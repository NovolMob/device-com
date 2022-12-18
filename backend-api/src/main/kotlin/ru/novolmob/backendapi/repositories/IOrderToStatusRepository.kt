package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.OrderToStatusEntityId

interface IOrderToStatusRepository: ICrudRepository<OrderToStatusEntityId, OrderToStatusEntityModel, OrderToStatusEntityCreateModel, OrderToStatusEntityUpdateModel> {
    suspend fun getStatuses(orderId: OrderId, language: Language): Either<BackendException, List<OrderStatusFullModel>>
}