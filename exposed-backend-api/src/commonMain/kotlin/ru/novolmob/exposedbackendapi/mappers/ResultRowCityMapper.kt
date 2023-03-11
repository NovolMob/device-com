package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.CityModel
import ru.novolmob.exposeddatabase.tables.Cities

class ResultRowCityMapper: Mapper<ResultRow, CityModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, CityModel> =
        CityModel(
            id = input[Cities.id].value
        ).right()
}