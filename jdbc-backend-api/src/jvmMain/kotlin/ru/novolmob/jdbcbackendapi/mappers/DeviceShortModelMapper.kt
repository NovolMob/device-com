package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceShortModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class DeviceShortModelMapper: Mapper<ResultSet, DeviceShortModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, DeviceShortModel> =
        Either.backend {
            DeviceShortModel(
                id = input get DetailView.DeviceDetailView.id,
                title = input get DetailView.DeviceDetailView.title,
                description = input get DetailView.DeviceDetailView.description,
                amount = input get DetailView.DeviceDetailView.amount,
                price = input get DetailView.DeviceDetailView.price
            )
        }
}