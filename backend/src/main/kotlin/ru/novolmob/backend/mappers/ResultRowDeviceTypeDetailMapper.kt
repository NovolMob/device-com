package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.exposeddatabase.tables.DeviceTypeDetails

class ResultRowDeviceTypeDetailMapper: Mapper<ResultRow, DeviceTypeDetailModel> {
    override fun invoke(input: ResultRow): Either<BackendException, DeviceTypeDetailModel> =
        DeviceTypeDetailModel(
            id = input[DeviceTypeDetails.id].value,
            deviceTypeId = input[DeviceTypeDetails.deviceType].value,
            title = input[DeviceTypeDetails.title],
            description = input[DeviceTypeDetails.description],
            language = input[DeviceTypeDetails.language]
        ).right()
}