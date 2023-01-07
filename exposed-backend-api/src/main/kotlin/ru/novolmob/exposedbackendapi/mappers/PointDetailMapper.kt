package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.exposeddatabase.entities.PointDetail

class PointDetailMapper: Mapper<PointDetail, PointDetailModel> {
    override fun invoke(input: PointDetail): Either<AbstractBackendException, PointDetailModel> =
        PointDetailModel(
            id = input.id.value,
            pointId = input.point.id.value,
            address = input.address,
            schedule = input.schedule,
            description = input.description,
            language = input.language
        ).right()
}