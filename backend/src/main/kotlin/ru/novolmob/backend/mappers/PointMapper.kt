package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.PointModel
import ru.novolmob.database.entities.Point

class PointMapper: Mapper<Point, PointModel> {
    override fun invoke(input: Point): Either<BackendException, PointModel> =
        PointModel(
            id = input.id.value
        ).right()
}