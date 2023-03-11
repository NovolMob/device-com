package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.PointDetails
import java.sql.ResultSet

class PointDetailMapper: Mapper<ResultSet, PointDetailModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, PointDetailModel> =
        Either.backend {
            PointDetailModel(
                id = input get PointDetails.id,
                parentId = input get PointDetails.parentId,
                address = input get PointDetails.address,
                schedule = input get PointDetails.schedule,
                description = input get PointDetails.description,
                language = input get PointDetails.language,
            )
        }
}