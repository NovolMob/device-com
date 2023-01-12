package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderShortModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.OrderView
import java.sql.ResultSet

class BlankOrderShortModelMapper: Mapper<ResultSet, OrderShortModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, OrderShortModel> =
        Either.backend {
            OrderShortModel(
                id = input get OrderView.id,
                point = input get OrderView.pointAddress,
                list = emptyList(),
                totalCost = input get OrderView.totalCost,
                status = null,
                active = true,
                creationTime = input get OrderView.creationTime,
            )
        }
}