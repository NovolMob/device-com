package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.DeviceModel
import ru.novolmob.database.tables.Devices

class ResultRowDeviceMapper: Mapper<ResultRow, DeviceModel> {
    override fun invoke(input: ResultRow): Either<BackendException, DeviceModel> =
        DeviceModel(
            id = input[Devices.id].value,
            article = input[Devices.code],
            typeId = input[Devices.type].value,
            price = input[Devices.price]
        ).right()
}