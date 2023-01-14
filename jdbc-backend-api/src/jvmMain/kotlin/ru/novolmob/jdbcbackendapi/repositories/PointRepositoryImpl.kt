package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.failedToCreatePoint
import ru.novolmob.backendapi.exceptions.pointByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IPointRepository
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.jdbcbackendapi.utils.RepositoryUtil
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.list
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateTableFunction
import ru.novolmob.jdbcdatabase.tables.Points
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class PointRepositoryImpl(
    val mapper: Mapper<ResultSet, PointModel>,
    val shortModelMapper: Mapper<ResultSet, PointShortModel>,
    val fullModelMapper: Mapper<ResultSet, PointFullModel>
): IPointRepository {
    override suspend fun getFull(
        id: PointId,
        language: Language
    ): Either<AbstractBackendException, PointFullModel> =
        DetailView.PointDetailView.select(id, language)  { fold(ifEmpty = { pointByIdNotFound(id) }, fullModelMapper::invoke) }

    override suspend fun getShort(
        id: PointId,
        language: Language
    ): Either<AbstractBackendException, PointShortModel> =
        DetailView.PointDetailView.select(id, language)  { fold(ifEmpty = { pointByIdNotFound(id) }, shortModelMapper::invoke) }

    override suspend fun getByCity(
        cityId: CityId,
        language: Language
    ): Either<AbstractBackendException, List<PointShortModel>> =
        DetailView.PointDetailView.select(cityId, language)  { list(shortModelMapper) }

    override suspend fun getAll(
        pagination: Pagination,
        language: Language
    ): Either<AbstractBackendException, Page<PointShortModel>> =
        RepositoryUtil.getAll(DetailView.PointDetailView, pagination, language, shortModelMapper)

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<PointModel>> =
        RepositoryUtil.getAll(Points, pagination, mapper)

    override suspend fun get(id: PointId): Either<AbstractBackendException, PointModel> =
        Points.select(id) { fold(ifEmpty = { pointByIdNotFound(id) }, mapper::invoke) }

    override suspend fun post(createModel: PointCreateModel): Either<AbstractBackendException, PointModel> =
        CreationOrUpdateTableFunction.CreationOrUpdatePointFunction.call(
            cityId = createModel.cityId
        ) { fold(ifEmpty = { failedToCreatePoint() }, mapper::invoke) }

    override suspend fun post(
        id: PointId,
        createModel: PointCreateModel
    ): Either<AbstractBackendException, PointModel> =
        CreationOrUpdateTableFunction.CreationOrUpdatePointFunction.call(
            id = id, cityId = createModel.cityId
        ) { fold(ifEmpty = { failedToCreatePoint() }, mapper::invoke) }

    override suspend fun put(id: PointId, updateModel: PointUpdateModel): Either<AbstractBackendException, PointModel> =
        Either.backend {
            if (listOf(updateModel.cityId).any { it != null }) {
                Points.update(id = id, updateModel.cityId)
            }
        }.flatMap { get(id) }

    override suspend fun delete(id: PointId): Either<AbstractBackendException, Boolean> =
        (Points.delete(id) > 0).right()
}