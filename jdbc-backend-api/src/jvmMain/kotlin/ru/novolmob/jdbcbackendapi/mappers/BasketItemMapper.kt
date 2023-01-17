package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.BasketItemModel
import ru.novolmob.backendapi.models.DeviceShortModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.BasketView
import java.sql.ResultSet

class BasketItemMapper: Mapper<ResultSet, BasketItemModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, BasketItemModel> =
        Either.backend {
            BasketItemModel(
                device = DeviceShortModel(
                    id = input get BasketView.deviceId,
                    title = input get BasketView.title,
                    description = input get BasketView.description,
                    amount = input get BasketView.amount,
                    price = input get BasketView.price
                ),
                amount = input get BasketView.amountInBasket
            )
        }
}