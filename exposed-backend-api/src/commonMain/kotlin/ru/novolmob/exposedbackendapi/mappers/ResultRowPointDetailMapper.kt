package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.exposeddatabase.tables.details.PointDetails

class ResultRowPointDetailMapper: Mapper<ResultRow, PointDetailModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, PointDetailModel> =
        PointDetailModel(
            id = input[PointDetails.id].value,
            pointId = input[PointDetails.parent].value,
            address = input[PointDetails.address],
            schedule = input[PointDetails.schedule],
            description = input[PointDetails.description],
            language = input[PointDetails.language]
        ).right()
}