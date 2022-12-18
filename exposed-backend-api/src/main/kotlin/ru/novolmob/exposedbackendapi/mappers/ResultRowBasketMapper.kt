package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.BasketModel
import ru.novolmob.exposeddatabase.tables.Baskets

class ResultRowBasketMapper: Mapper<ResultRow, BasketModel> {
    override fun invoke(input: ResultRow): Either<BackendException, BasketModel> =
        BasketModel(
            id = input[Baskets.id].value,
            userId = input[Baskets.user].value,
            deviceId = input[Baskets.device].value,
            amount = input[Baskets.amount]
        ).right()
}