package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.exposeddatabase.entities.details.DeviceTypeDetail

class DeviceTypeDetailMapper: Mapper<DeviceTypeDetail, DeviceTypeDetailModel> {
    override fun invoke(input: DeviceTypeDetail): Either<AbstractBackendException, DeviceTypeDetailModel> =
        DeviceTypeDetailModel(
            id = input.id.value,
            deviceTypeId = input.parent.id.value,
            title = input.title,
            description = input.description,
            language = input.language
        ).right()
}