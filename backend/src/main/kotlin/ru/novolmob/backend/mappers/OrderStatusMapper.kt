package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderStatusModel
import ru.novolmob.database.entities.OrderStatus

class OrderStatusMapper: Mapper<OrderStatus, OrderStatusModel> {
    override fun invoke(input: OrderStatus): Either<BackendException, OrderStatusModel> =
        OrderStatusModel(
            id = input.id.value,
            orderId = input.order.id.value,
            workerId = input.worker.id.value
        ).right()
}