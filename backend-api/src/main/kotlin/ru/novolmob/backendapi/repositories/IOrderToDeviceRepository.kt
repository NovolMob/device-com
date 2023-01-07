package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.OrderItemModel
import ru.novolmob.backendapi.models.OrderToDeviceEntityCreateModel
import ru.novolmob.backendapi.models.OrderToDeviceEntityModel
import ru.novolmob.backendapi.models.OrderToDeviceEntityUpdateModel
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.OrderToDeviceEntityId

interface IOrderToDeviceRepository: ICrudRepository<OrderToDeviceEntityId, OrderToDeviceEntityModel, OrderToDeviceEntityCreateModel, OrderToDeviceEntityUpdateModel> {
    suspend fun getDevices(orderId: OrderId, language: Language): Either<AbstractBackendException, List<OrderItemModel>>
    suspend fun removeAllFor(orderId: OrderId): Either<AbstractBackendException, Boolean>
}