package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.exposeddatabase.tables.details.DeviceTypeDetails

class ResultRowDeviceTypeDetailMapper: Mapper<ResultRow, DeviceTypeDetailModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, DeviceTypeDetailModel> =
        DeviceTypeDetailModel(
            id = input[DeviceTypeDetails.id].value,
            parentId = input[DeviceTypeDetails.parent].value,
            title = input[DeviceTypeDetails.title],
            description = input[DeviceTypeDetails.description],
            language = input[DeviceTypeDetails.language]
        ).right()
}