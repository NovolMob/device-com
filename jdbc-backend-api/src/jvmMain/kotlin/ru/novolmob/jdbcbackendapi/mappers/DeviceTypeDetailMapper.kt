package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.DeviceTypeDetails
import java.sql.ResultSet

class DeviceTypeDetailMapper: Mapper<ResultSet, DeviceTypeDetailModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, DeviceTypeDetailModel> =
        Either.backend {
            DeviceTypeDetailModel(
                id = input get DeviceTypeDetails.id,
                parentId = input get DeviceTypeDetails.parentId,
                title = input get DeviceTypeDetails.title,
                description = input get DeviceTypeDetails.description,
                language = input get DeviceTypeDetails.language,
            )
        }
}