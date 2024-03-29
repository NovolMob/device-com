package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderModel
import ru.novolmob.exposeddatabase.tables.Orders

class ResultRowOrderMapper: Mapper<ResultRow, OrderModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, OrderModel> =
        OrderModel(
            id = input[Orders.id].value,
            userId = input[Orders.user].value,
            pointId = input[Orders.point].value,
            totalCost = input[Orders.totalCost]
        ).right()
}