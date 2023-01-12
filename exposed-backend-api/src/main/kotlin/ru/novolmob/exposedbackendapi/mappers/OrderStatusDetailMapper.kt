package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderStatusDetailModel
import ru.novolmob.exposeddatabase.entities.details.OrderStatusDetail

class OrderStatusDetailMapper: Mapper<OrderStatusDetail, OrderStatusDetailModel> {
    override fun invoke(input: OrderStatusDetail): Either<AbstractBackendException, OrderStatusDetailModel> =
        OrderStatusDetailModel(
            id = input.id.value,
            orderStatusId = input.parent.id.value,
            title = input.title,
            description = input.description,
            language = input.language
        ).right()
}