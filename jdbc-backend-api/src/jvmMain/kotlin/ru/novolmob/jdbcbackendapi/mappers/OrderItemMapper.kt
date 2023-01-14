package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderItemModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.OrderToDeviceView
import java.sql.ResultSet

class OrderItemMapper: Mapper<ResultSet, OrderItemModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, OrderItemModel> =
        Either.backend {
            OrderItemModel(
                deviceId = input get OrderToDeviceView.deviceId,
                title = input get OrderToDeviceView.deviceTitle,
                description = input get OrderToDeviceView.deviceDescription,
                amount = input get OrderToDeviceView.amount,
                priceForOne = input get OrderToDeviceView.priceForOne,
            )
        }

}