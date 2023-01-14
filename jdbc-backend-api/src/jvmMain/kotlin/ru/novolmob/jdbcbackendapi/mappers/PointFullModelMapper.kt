package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.CityDetailModel
import ru.novolmob.backendapi.models.CityFullModel
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.backendapi.models.PointFullModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class PointFullModelMapper: Mapper<ResultSet, PointFullModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, PointFullModel> =
        Either.backend {
            PointFullModel(
                id = input get DetailView.PointDetailView.id,
                city = CityFullModel(
                    id = input get DetailView.PointDetailView.cityId,
                    detail = CityDetailModel(
                        id = input get DetailView.PointDetailView.cityDetailId,
                        cityId = input get DetailView.PointDetailView.cityId,
                        title = input get DetailView.PointDetailView.cityTitle,
                        language = input get DetailView.PointDetailView.cityLanguage,
                    ),
                ),
                detail = PointDetailModel(
                    id = input get DetailView.PointDetailView.detailId,
                    pointId = input get DetailView.PointDetailView.id,
                    address = input get DetailView.PointDetailView.address,
                    schedule = input get DetailView.PointDetailView.schedule,
                    description = input get DetailView.PointDetailView.description,
                    language = input get DetailView.PointDetailView.language,
                )
            )
        }
}