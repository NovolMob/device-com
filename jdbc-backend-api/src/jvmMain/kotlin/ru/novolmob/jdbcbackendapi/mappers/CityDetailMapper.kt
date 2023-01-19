package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.CityDetailModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.CityDetails
import java.sql.ResultSet

class CityDetailMapper: Mapper<ResultSet, CityDetailModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, CityDetailModel> =
        Either.backend {
            CityDetailModel(
                id = input get CityDetails.id,
                parentId = input get CityDetails.parentId,
                title = input get CityDetails.title,
                language = input get CityDetails.language,
            )
        }
}