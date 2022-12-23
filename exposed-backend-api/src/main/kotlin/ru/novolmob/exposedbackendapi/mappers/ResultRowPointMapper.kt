package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.PointModel
import ru.novolmob.exposeddatabase.tables.Points

class ResultRowPointMapper: Mapper<ResultRow, PointModel> {
    override fun invoke(input: ResultRow): Either<BackendException, PointModel> =
        PointModel(
            id = input[Points.id].value,
            city = input[Points.city]
        ).right()
}