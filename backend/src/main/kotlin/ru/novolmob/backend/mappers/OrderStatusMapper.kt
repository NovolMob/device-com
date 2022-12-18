package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderStatusModel
import ru.novolmob.exposeddatabase.entities.OrderStatus

class OrderStatusMapper: Mapper<OrderStatus, OrderStatusModel> {
    override fun invoke(input: OrderStatus): Either<BackendException, OrderStatusModel> =
        OrderStatusModel(
            id = input.id.value,
            active = input.active
        ).right()
}