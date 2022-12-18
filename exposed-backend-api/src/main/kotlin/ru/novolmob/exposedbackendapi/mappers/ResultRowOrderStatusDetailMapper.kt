package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderStatusDetailModel
import ru.novolmob.exposeddatabase.tables.OrderStatusDetails

class ResultRowOrderStatusDetailMapper: Mapper<ResultRow, OrderStatusDetailModel> {
    override fun invoke(input: ResultRow): Either<BackendException, OrderStatusDetailModel> =
        OrderStatusDetailModel(
            id = input[OrderStatusDetails.id].value,
            orderStatusId = input[OrderStatusDetails.orderStatus].value,
            title = input[OrderStatusDetails.title],
            description = input[OrderStatusDetails.description],
            language = input[OrderStatusDetails.language]
        ).right()
}