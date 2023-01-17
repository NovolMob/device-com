package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.Devices
import java.sql.ResultSet

class DeviceMapper: Mapper<ResultSet, DeviceModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, DeviceModel> =
        Either.backend {
            DeviceModel(
                id = input get Devices.id,
                article = input get Devices.code,
                typeId = input get Devices.typeId,
                amount = input get Devices.amount,
                price = input get Devices.price,
            )
        }
}