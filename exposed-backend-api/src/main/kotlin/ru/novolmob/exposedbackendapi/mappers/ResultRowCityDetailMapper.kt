package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.CityDetailModel
import ru.novolmob.exposeddatabase.tables.CityDetails

class ResultRowCityDetailMapper: Mapper<ResultRow, CityDetailModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, CityDetailModel> =
        CityDetailModel(
            id = input[CityDetails.id].value,
            cityId = input[CityDetails.city].value,
            title = input[CityDetails.title],
            language = input[CityDetails.language]
        ).right()
}