package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.PointModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.Points
import java.sql.ResultSet

class PointMapper: Mapper<ResultSet, PointModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, PointModel> =
        Either.backend {
            PointModel(
                id = input get Points.id,
                cityId = input get Points.cityId,
            )
        }
}