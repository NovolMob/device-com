package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderStatusDetailModel
import ru.novolmob.database.entities.OrderStatusDetail

class OrderStatusDetailMapper: Mapper<OrderStatusDetail, OrderStatusDetailModel> {
    override fun invoke(input: OrderStatusDetail): Either<BackendException, OrderStatusDetailModel> =
        OrderStatusDetailModel(
            id = input.id.value,
            orderStatusId = input.orderStatus.id.value,
            title = input.title,
            description = input.description,
            language = input.language
        ).right()
}