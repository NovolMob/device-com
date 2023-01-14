package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.backendapi.models.DeviceTypeFullModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class DeviceTypeFullModelMapper: Mapper<ResultSet, DeviceTypeFullModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, DeviceTypeFullModel> =
        Either.backend {
            DeviceTypeFullModel(
                id = input get DetailView.DeviceTypeDetailView.id,
                detail = DeviceTypeDetailModel(
                    id = input get DetailView.DeviceTypeDetailView.detailId,
                    deviceTypeId = input get DetailView.DeviceTypeDetailView.id,
                    title = input get DetailView.DeviceTypeDetailView.title,
                    description = input get DetailView.DeviceTypeDetailView.description,
                    language = input get DetailView.DeviceTypeDetailView.language,
                ),
            )
        }
}