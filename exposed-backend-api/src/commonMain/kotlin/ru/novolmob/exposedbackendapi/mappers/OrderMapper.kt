package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderModel
import ru.novolmob.exposeddatabase.entities.Order

class OrderMapper: Mapper<Order, OrderModel> {
    override fun invoke(input: Order): Either<AbstractBackendException, OrderModel> =
        OrderModel(
            id = input.id.value,
            userId = input.user.id.value,
            pointId = input.point.id.value,
            totalCost = input.totalCost
        ).right()
}