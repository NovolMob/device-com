package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.backendapi.models.DeviceFullModel
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.backendapi.models.DeviceTypeFullModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class DeviceFullModelMapper: Mapper<ResultSet, DeviceFullModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, DeviceFullModel> =
        Either.backend {
            DeviceFullModel(
                id = input get DetailView.DeviceDetailView.id,
                article = input get DetailView.DeviceDetailView.code,
                type = DeviceTypeFullModel(
                    id = input get DetailView.DeviceDetailView.typeId,
                    detail = DeviceTypeDetailModel(
                        id = input get DetailView.DeviceDetailView.typeDetailId,
                        parentId = input get DetailView.DeviceDetailView.typeId,
                        title = input get DetailView.DeviceDetailView.typeTitle,
                        description = input get DetailView.DeviceDetailView.typeDescription,
                        language = input get DetailView.DeviceDetailView.typeLanguage,
                    )
                ),
                detailModel = DeviceDetailModel(
                    id = input get DetailView.DeviceDetailView.detailId,
                    parentId = input get DetailView.DeviceDetailView.id,
                    title = input get DetailView.DeviceDetailView.title,
                    description = input get DetailView.DeviceDetailView.description,
                    features = input get DetailView.DeviceDetailView.features,
                    language = input get DetailView.DeviceDetailView.language,
                ),
                amount = input get DetailView.DeviceDetailView.amount,
                price = input get DetailView.DeviceDetailView.price,
            )
        }
}