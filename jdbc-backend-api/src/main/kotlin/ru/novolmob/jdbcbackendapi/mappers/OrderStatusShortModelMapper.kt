package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderStatusShortModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.OrderToStatusView
import java.sql.ResultSet

class OrderStatusShortModelMapper: Mapper<ResultSet, OrderStatusShortModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, OrderStatusShortModel> =
        Either.backend {
            OrderStatusShortModel(
                id = input get OrderToStatusView.orderStatusId,
                active = input get OrderToStatusView.orderStatusActive,
                dateTime = input get OrderToStatusView.creationTime,
                title = input get OrderToStatusView.orderStatusTitle,
                description = input get OrderToStatusView.orderStatusDescription,
            )
        }
}