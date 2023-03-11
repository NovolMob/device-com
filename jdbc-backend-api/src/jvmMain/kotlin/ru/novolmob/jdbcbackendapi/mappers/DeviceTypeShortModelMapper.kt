package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceTypeShortModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class DeviceTypeShortModelMapper: Mapper<ResultSet, DeviceTypeShortModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, DeviceTypeShortModel> =
        Either.backend {
            DeviceTypeShortModel(
                id = input get DetailView.DeviceTypeDetailView.id,
                title = input get DetailView.DeviceTypeDetailView.title,
            )
        }
}