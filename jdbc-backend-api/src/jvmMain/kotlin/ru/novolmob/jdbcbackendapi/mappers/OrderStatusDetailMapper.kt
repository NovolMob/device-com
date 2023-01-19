package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderStatusDetailModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.OrderStatusDetails
import java.sql.ResultSet

class OrderStatusDetailMapper: Mapper<ResultSet, OrderStatusDetailModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, OrderStatusDetailModel> =
        Either.backend {
            OrderStatusDetailModel(
                id = input get OrderStatusDetails.id,
                parentId = input get OrderStatusDetails.parentId,
                title = input get OrderStatusDetails.title,
                description = input get OrderStatusDetails.description,
                language = input get OrderStatusDetails.language,
            )
        }
}