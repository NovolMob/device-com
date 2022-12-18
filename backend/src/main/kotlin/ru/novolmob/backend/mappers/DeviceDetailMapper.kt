package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.exposeddatabase.entities.DeviceDetail

class DeviceDetailMapper: Mapper<DeviceDetail, DeviceDetailModel> {
    override fun invoke(input: DeviceDetail): Either<BackendException, DeviceDetailModel> =
        DeviceDetailModel(
            id = input.id.value,
            deviceId = input.device.id.value,
            title = input.title,
            description = input.description,
            features = input.features,
            language = input.language
        ).right()
}