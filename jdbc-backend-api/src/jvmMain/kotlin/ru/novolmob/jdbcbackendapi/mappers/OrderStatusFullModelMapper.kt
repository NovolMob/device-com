package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderStatusDetailModel
import ru.novolmob.backendapi.models.OrderStatusFullModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.OrderToStatusView
import java.sql.ResultSet

class OrderStatusFullModelMapper: Mapper<ResultSet, OrderStatusFullModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, OrderStatusFullModel> =
        Either.backend {
            OrderStatusFullModel(
                id = input get OrderToStatusView.orderStatusId,
                active = input get OrderToStatusView.orderStatusActive,
                dateTime = input get OrderToStatusView.creationTime,
                detail = OrderStatusDetailModel(
                    id = input get OrderToStatusView.orderStatusDetailId,
                    parentId = input get OrderToStatusView.orderStatusId,
                    title = input get OrderToStatusView.orderStatusTitle,
                    description = input get OrderToStatusView.orderStatusDescription,
                    language = input get OrderToStatusView.orderStatusLanguage,
                )
            )
        }
}