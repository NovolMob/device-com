package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.CityShortModel
import ru.novolmob.backendapi.models.PointShortModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class PointShortModelMapper: Mapper<ResultSet, PointShortModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, PointShortModel> =
        Either.backend {
            PointShortModel(
                id = input get DetailView.PointDetailView.id,
                city = CityShortModel(
                    id = input get DetailView.PointDetailView.cityId,
                    title = input get DetailView.PointDetailView.cityTitle
                ),
                address = input get DetailView.PointDetailView.address,
                schedule = input get DetailView.PointDetailView.schedule
            )
        }
}