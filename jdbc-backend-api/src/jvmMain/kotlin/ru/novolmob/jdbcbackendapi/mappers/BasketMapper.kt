package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.BasketModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.Baskets
import java.sql.ResultSet

class BasketMapper: Mapper<ResultSet, BasketModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, BasketModel> =
        Either.backend {
            BasketModel(
                id = input get Baskets.id,
                userId = input get Baskets.userId,
                deviceId = input get Baskets.deviceId,
                amount = input get Baskets.amount
            )
        }
}