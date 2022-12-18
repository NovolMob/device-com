package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.DeviceTypeModel
import ru.novolmob.exposeddatabase.entities.DeviceType

class DeviceTypeMapper: Mapper<DeviceType, DeviceTypeModel> {
    override fun invoke(input: DeviceType): Either<BackendException, DeviceTypeModel> =
        DeviceTypeModel(
            id = input.id.value,
        ).right()
}