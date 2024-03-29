package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.exposeddatabase.tables.details.DeviceDetails

class ResultRowDeviceDetailMapper: Mapper<ResultRow, DeviceDetailModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, DeviceDetailModel> =
        DeviceDetailModel(
            id = input[DeviceDetails.id].value,
            parentId = input[DeviceDetails.parent].value,
            title = input[DeviceDetails.title],
            description = input[DeviceDetails.description],
            features = input[DeviceDetails.features],
            language = input[DeviceDetails.language]
        ).right()
}