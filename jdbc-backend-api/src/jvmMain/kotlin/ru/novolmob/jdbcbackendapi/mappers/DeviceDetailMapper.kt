package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.DeviceDetails
import java.sql.ResultSet

class DeviceDetailMapper: Mapper<ResultSet, DeviceDetailModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, DeviceDetailModel> =
        Either.backend {
            DeviceDetailModel(
                id = input get DeviceDetails.id,
                parentId = input get DeviceDetails.parentId,
                title = input get DeviceDetails.title,
                description = input get DeviceDetails.description,
                features = input get DeviceDetails.features,
                language = input get DeviceDetails.language,
            )
        }
}