package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.exposeddatabase.entities.DeviceTypeDetail

class DeviceTypeDetailMapper: Mapper<DeviceTypeDetail, DeviceTypeDetailModel> {
    override fun invoke(input: DeviceTypeDetail): Either<BackendException, DeviceTypeDetailModel> =
        DeviceTypeDetailModel(
            id = input.id.value,
            deviceTypeId = input.deviceType.id.value,
            title = input.title,
            description = input.description,
            language = input.language
        ).right()
}