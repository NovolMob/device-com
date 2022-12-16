package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.DeviceTypeModel
import ru.novolmob.database.tables.DeviceTypes

class ResultRowDeviceTypeMapper: Mapper<ResultRow, DeviceTypeModel> {
    override fun invoke(input: ResultRow): Either<BackendException, DeviceTypeModel> =
        DeviceTypeModel(
            id = input[DeviceTypes.id].value,
        ).right()
}