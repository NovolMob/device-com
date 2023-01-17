package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceTypeModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.DeviceTypes
import java.sql.ResultSet

class DeviceTypeMapper: Mapper<ResultSet, DeviceTypeModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, DeviceTypeModel> =
        Either.backend {
            DeviceTypeModel(
                id = input get  DeviceTypes.id
            )
        }
}