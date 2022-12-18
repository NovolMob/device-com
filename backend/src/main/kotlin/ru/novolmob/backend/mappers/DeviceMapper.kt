package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.DeviceModel
import ru.novolmob.exposeddatabase.entities.Device

class DeviceMapper: Mapper<Device, DeviceModel> {
    override fun invoke(input: Device): Either<BackendException, DeviceModel> =
        DeviceModel(
            id = input.id.value,
            article = input.article,
            typeId = input.type.id.value,
            price = input.price
        ).right()
}