package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.PointToDeviceEntityModel
import ru.novolmob.exposeddatabase.entities.PointToDeviceEntity

class PointToDeviceMapper: Mapper<PointToDeviceEntity, PointToDeviceEntityModel> {
    override fun invoke(input: PointToDeviceEntity): Either<BackendException, PointToDeviceEntityModel> =
        PointToDeviceEntityModel(
            id = input.id.value,
            pointId = input.point.id.value,
            deviceId = input.device.id.value,
            amount = input.amount
        ).right()
}