package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceTypeModel
import ru.novolmob.exposeddatabase.entities.DeviceType

class DeviceTypeMapper: Mapper<DeviceType, DeviceTypeModel> {
    override fun invoke(input: DeviceType): Either<AbstractBackendException, DeviceTypeModel> =
        DeviceTypeModel(
            id = input.id.value,
        ).right()
}