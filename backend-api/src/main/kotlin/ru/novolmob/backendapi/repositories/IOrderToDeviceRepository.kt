package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.OrderItemModel
import ru.novolmob.backendapi.models.OrderToDeviceEntityCreateModel
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId

interface IOrderToDeviceRepository: IRepository {
    suspend fun getDevices(orderId: OrderId, language: Language): Either<AbstractBackendException, List<OrderItemModel>>
    suspend fun post(createModel: OrderToDeviceEntityCreateModel): Either<AbstractBackendException, Unit>
}