package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.PointItemModel
import ru.novolmob.exposeddatabase.entities.PointToDeviceEntity

class PointToDeviceItemMapper: Mapper<PointToDeviceEntity, PointItemModel> {
    override fun invoke(input: PointToDeviceEntity): Either<BackendException, PointItemModel> =
        PointItemModel(
            deviceId = input.device.id.value,
            amount = input.amount
        ).right()
}