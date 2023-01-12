package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceModel
import ru.novolmob.exposeddatabase.entities.Device

class DeviceMapper: Mapper<Device, DeviceModel> {
    override fun invoke(input: Device): Either<AbstractBackendException, DeviceModel> =
        DeviceModel(
            id = input.id.value,
            article = input.article,
            typeId = input.type.id.value,
            price = input.price,
            amount = input.amount
        ).right()
}