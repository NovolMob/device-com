package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.exposeddatabase.entities.details.DeviceDetail

class DeviceDetailMapper: Mapper<DeviceDetail, DeviceDetailModel> {
    override fun invoke(input: DeviceDetail): Either<AbstractBackendException, DeviceDetailModel> =
        DeviceDetailModel(
            id = input.id.value,
            deviceId = input.parent.id.value,
            title = input.title,
            description = input.description,
            features = input.features,
            language = input.language
        ).right()
}