package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderItemModel
import ru.novolmob.exposeddatabase.entities.OrderToDeviceEntity

class OrderToDeviceItemMapper: Mapper<OrderToDeviceEntity, OrderItemModel> {
    override fun invoke(input: OrderToDeviceEntity): Either<BackendException, OrderItemModel> =
        OrderItemModel(
            deviceId = input.device.id.value,
            amount = input.amount,
            priceForOne = input.priceForOne
        ).right()
}