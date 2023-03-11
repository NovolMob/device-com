package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.CityDetailModel
import ru.novolmob.backendapi.models.CityFullModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class CityFullModelMapper: Mapper<ResultSet, CityFullModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, CityFullModel> =
        Either.backend {
            CityFullModel(
                id = input get DetailView.CityDetailView.id,
                detail = CityDetailModel(
                    id = input get DetailView.CityDetailView.detailId,
                    parentId = input get DetailView.CityDetailView.id,
                    title = input get DetailView.CityDetailView.title,
                    language = input get DetailView.CityDetailView.language
                )
            )
        }
}