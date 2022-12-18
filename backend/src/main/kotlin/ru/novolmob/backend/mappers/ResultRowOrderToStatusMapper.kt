package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.OrderToStatusEntityModel
import ru.novolmob.exposeddatabase.tables.OrderToStatusTable

class ResultRowOrderToStatusMapper: Mapper<ResultRow, OrderToStatusEntityModel> {
    override fun invoke(input: ResultRow): Either<BackendException, OrderToStatusEntityModel> =
        OrderToStatusEntityModel(
            id = input[OrderToStatusTable.id].value,
            orderId = input[OrderToStatusTable.order].value,
            status = input[OrderToStatusTable.orderStatus].value,
            workerId = input[OrderToStatusTable.worker].value
        ).right()
}