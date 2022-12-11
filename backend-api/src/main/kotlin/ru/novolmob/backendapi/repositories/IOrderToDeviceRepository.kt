package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderItemModel
import ru.novolmob.backendapi.models.OrderToDeviceEntityCreateModel
import ru.novolmob.backendapi.models.OrderToDeviceEntityModel
import ru.novolmob.backendapi.models.OrderToDeviceEntityUpdateModel
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.OrderToDeviceEntityId

interface IOrderToDeviceRepository: ICrudRepository<OrderToDeviceEntityId, OrderToDeviceEntityModel, OrderToDeviceEntityCreateModel, OrderToDeviceEntityUpdateModel> {
    suspend fun getDevices(orderId: OrderId): Either<BackendException, List<OrderItemModel>>
}