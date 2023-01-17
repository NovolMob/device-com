package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.CityShortModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class CityShortModelMapper: Mapper<ResultSet, CityShortModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, CityShortModel> =
        Either.backend {
            CityShortModel(
                id = input get DetailView.CityDetailView.id,
                title = input get DetailView.CityDetailView.title
            )
        }
}