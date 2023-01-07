package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.PointModel
import ru.novolmob.exposeddatabase.entities.Point

class PointMapper: Mapper<Point, PointModel> {
    override fun invoke(input: Point): Either<AbstractBackendException, PointModel> =
        PointModel(
            id = input.id.value,
            cityId = input.city.id.value
        ).right()
}