package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.OrderStatusModel
import ru.novolmob.exposeddatabase.tables.OrderStatuses

class ResultRowOrderStatusMapper: Mapper<ResultRow, OrderStatusModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, OrderStatusModel> =
        OrderStatusModel(
            id = input[OrderStatuses.id].value,
            active = input[OrderStatuses.active]
        ).right()
}