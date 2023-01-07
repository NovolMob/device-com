package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.DeviceModel
import ru.novolmob.exposeddatabase.tables.Devices

class ResultRowDeviceMapper: Mapper<ResultRow, DeviceModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, DeviceModel> =
        DeviceModel(
            id = input[Devices.id].value,
            article = input[Devices.code],
            typeId = input[Devices.type].value,
            price = input[Devices.price],
            amount = input[Devices.amount]
        ).right()
}