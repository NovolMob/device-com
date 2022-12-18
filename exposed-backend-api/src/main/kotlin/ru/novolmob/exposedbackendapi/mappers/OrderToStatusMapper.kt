package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderToStatusEntityModel
import ru.novolmob.exposeddatabase.entities.OrderToStatusEntity

class OrderToStatusMapper: Mapper<OrderToStatusEntity, OrderToStatusEntityModel> {
    override fun invoke(input: OrderToStatusEntity): Either<BackendException, OrderToStatusEntityModel> =
        OrderToStatusEntityModel(
            id = input.id.value,
            orderId = input.order.id.value,
            status = input.status.id.value,
            workerId = input.worker.id.value
        ).right()
}