package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.exposeddatabase.tables.PointDetails

class ResultRowPointDetailMapper: Mapper<ResultRow, PointDetailModel> {
    override fun invoke(input: ResultRow): Either<BackendException, PointDetailModel> =
        PointDetailModel(
            id = input[PointDetails.id].value,
            pointId = input[PointDetails.point].value,
            address = input[PointDetails.address],
            schedule = input[PointDetails.schedule],
            description = input[PointDetails.description],
            language = input[PointDetails.language]
        ).right()
}