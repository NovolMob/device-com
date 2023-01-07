package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.ICityRepository
import ru.novolmob.backendapi.repositories.IPointDetailRepository
import ru.novolmob.backendapi.repositories.IPointRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.exposedbackendapi.exceptions.pointByIdNotFound
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.exposeddatabase.entities.Point
import ru.novolmob.exposeddatabase.tables.Points

class PointRepositoryImpl(
    val mapper: Mapper<Point, PointModel>,
    val resultRowMapper: Mapper<ResultRow, PointModel>,
    val cityRepository: ICityRepository,
    val pointDetailRepository: IPointDetailRepository
): IPointRepository {
    override suspend fun getFull(pointId: PointId, language: Language): Either<AbstractBackendException, PointFullModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(pointId)?.let{
                cityRepository.getFull(it.city.id.value, language).flatMap { city ->
                    pointDetailRepository.getDetailFor(pointId, language).flatMap { detail ->
                        PointFullModel(
                            id = pointId,
                            detail = detail,
                            city = city
                        ).right()
                    }
                }
            } ?: pointByIdNotFound(pointId).left()
        }

    override suspend fun getShort(pointId: PointId, language: Language): Either<AbstractBackendException, PointShortModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(pointId)?.let{
                cityRepository.getShort(it.city.id.value, language).flatMap { city ->
                    pointDetailRepository.getDetailFor(pointId, language).flatMap { detail ->
                        PointShortModel(
                            id = pointId,
                            address = detail.address,
                            schedule = detail.schedule,
                            city = city
                        ).right()
                    }
                }
            } ?: pointByIdNotFound(pointId).left()
        }

    override suspend fun getByCity(cityId: CityId, language: Language): Either<AbstractBackendException, List<PointShortModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            cityRepository.getShort(cityId, language).flatMap { city ->
                Point.find { Points.city eq cityId }
                    .parTraverseEither {
                        pointDetailRepository.getDetailFor(it.id.value, language).flatMap { detail ->
                            PointShortModel(
                                id = detail.pointId,
                                address = detail.address,
                                schedule = detail.schedule,
                                city = city
                            ).right()
                        }
                    }
            }
        }

    override suspend fun getAll(pagination: Pagination, language: Language): Either<AbstractBackendException, Page<PointShortModel>> =
        RepositoryUtil.generalGetAll(Points, pagination, resultRowMapper).flatMap { page ->
            page.list
                .groupBy { it.cityId }.right()
                .flatMap {
                    it.map { (cityId, list) ->
                        cityRepository.getShort(cityId, language).flatMap { city ->
                            list.parTraverseEither { point ->
                                pointDetailRepository.getDetailFor(point.id, language).flatMap { detail ->
                                    PointShortModel(
                                        id = detail.pointId,
                                        address = detail.address,
                                        schedule = detail.schedule,
                                        city = city
                                    ).right()
                                }
                            }
                        }
                    }.parTraverseEither { it }.flatMap {
                        it.flatten().right()
                    }.flatMap {
                        Page(
                            page = page.page,
                            size = it.size.toLong(),
                            list = it
                        ).right()
                    }
                }
        }

    override suspend fun get(id: PointId): Either<AbstractBackendException, PointModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(id)?.let(mapper::invoke) ?: pointByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<PointModel>> =
        RepositoryUtil.generalGetAll(Points, pagination, resultRowMapper)

    override suspend fun post(createModel: PointCreateModel): Either<AbstractBackendException, PointModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.new {

            }.let(mapper::invoke)
        }

    override suspend fun post(id: PointId, createModel: PointCreateModel): Either<AbstractBackendException, PointModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(id)?.apply {

            }?.let(mapper::invoke) ?: pointByIdNotFound(id).left()
        }

    override suspend fun put(id: PointId, updateModel: PointUpdateModel): Either<AbstractBackendException, PointModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(id)?.apply {

            }?.let(mapper::invoke) ?: pointByIdNotFound(id).left()
        }

    override suspend fun delete(id: PointId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}