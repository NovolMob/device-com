package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.CityModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.Cities
import java.sql.ResultSet

class CityMapper: Mapper<ResultSet, CityModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, CityModel> =
        Either.backend {
            CityModel(
                id = input get Cities.id
            )
        }
}