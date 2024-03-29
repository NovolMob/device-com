package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderToDeviceEntityModel
import ru.novolmob.exposeddatabase.entities.OrderToDeviceEntity

class OrderToDeviceMapper: Mapper<OrderToDeviceEntity, OrderToDeviceEntityModel> {
    override fun invoke(input: OrderToDeviceEntity): Either<AbstractBackendException, OrderToDeviceEntityModel> =
        OrderToDeviceEntityModel(
            id = input.id.value,
            orderId = input.order.id.value,
            deviceId = input.device.id.value,
            amount = input.amount,
            priceForOne = input.priceForOne
        ).right()
}