package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderStatusDetailModel
import ru.novolmob.exposeddatabase.tables.details.OrderStatusDetails

class ResultRowOrderStatusDetailMapper: Mapper<ResultRow, OrderStatusDetailModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, OrderStatusDetailModel> =
        OrderStatusDetailModel(
            id = input[OrderStatusDetails.id].value,
            parentId = input[OrderStatusDetails.parent].value,
            title = input[OrderStatusDetails.title],
            description = input[OrderStatusDetails.description],
            language = input[OrderStatusDetails.language]
        ).right()
}