package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.DeviceTypeModel
import ru.novolmob.exposeddatabase.tables.DeviceTypes

class ResultRowDeviceTypeMapper: Mapper<ResultRow, DeviceTypeModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, DeviceTypeModel> =
        DeviceTypeModel(
            id = input[DeviceTypes.id].value,
        ).right()
}