package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.OrderView
import java.sql.ResultSet

class BlankOrderFullModelMapper: Mapper<ResultSet, OrderFullModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, OrderFullModel> =
        Either.backend {
            OrderFullModel(
                id = input get OrderView.id,
                userId = input get OrderView.userId,
                point = PointFullModel(
                    id = input get OrderView.pointId,
                    city = CityFullModel(
                        id = input get OrderView.cityId,
                        detail = CityDetailModel(
                            id = input get OrderView.cityDetailId,
                            parentId = input get OrderView.cityId,
                            title = input get OrderView.cityTitle,
                            language = input get OrderView.cityLanguage
                        )
                    ),
                    detail = PointDetailModel(
                        id = input get OrderView.pointDetailId,
                        parentId = input get OrderView.pointId,
                        address = input get OrderView.pointAddress,
                        schedule = input get OrderView.pointSchedule,
                        description = input get OrderView.pointDescription,
                        language = input get OrderView.pointLanguage
                    )
                ),
                list = emptyList(),
                totalCost = input get OrderView.totalCost,
                lastStatus = null,
                creationTime = input get OrderView.creationTime,
            )
        }
}