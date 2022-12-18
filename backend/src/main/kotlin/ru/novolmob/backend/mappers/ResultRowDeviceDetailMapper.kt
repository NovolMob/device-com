package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.exposeddatabase.tables.DeviceDetails

class ResultRowDeviceDetailMapper: Mapper<ResultRow, DeviceDetailModel> {
    override fun invoke(input: ResultRow): Either<BackendException, DeviceDetailModel> =
        DeviceDetailModel(
            id = input[DeviceDetails.id].value,
            deviceId = input[DeviceDetails.device].value,
            title = input[DeviceDetails.title],
            description = input[DeviceDetails.description],
            features = input[DeviceDetails.features],
            language = input[DeviceDetails.language]
        ).right()
}