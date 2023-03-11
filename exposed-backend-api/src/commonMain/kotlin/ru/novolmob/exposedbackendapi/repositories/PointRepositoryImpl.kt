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
import ru.novolmob.backendapi.exceptions.pointByIdNotFound
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.ICityRepository
import ru.novolmob.backendapi.repositories.IPointDetailRepository
import ru.novolmob.backendapi.repositories.IPointRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.exposedbackendapi.utils.RepositoryUtil
import ru.novolmob.exposeddatabase.entities.Point
import ru.novolmob.exposeddatabase.tables.Points

class PointRepositoryImpl(
    mapper: Mapper<Point, PointModel>,
    resultRowMapper: Mapper<ResultRow, PointModel>,
    val cityRepository: ICityRepository,
    val pointDetailRepository: IPointDetailRepository
): IPointRepository, AbstractCrudRepository<PointId, Point.Companion, Point, PointModel, PointCreateModel, PointUpdateModel>(
    Point.Companion, mapper, resultRowMapper, ::pointByIdNotFound
) {
    override suspend fun getFull(id: PointId, language: Language): Either<AbstractBackendException, PointFullModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(id)?.let{
                cityRepository.getFull(it.city.id.value, language).flatMap { city ->
                    pointDetailRepository.getDetailFor(id, language).flatMap { detail ->
                        PointFullModel(
                            id = id,
                            detail = detail,
                            city = city
                        ).right()
                    }
                }
            } ?: pointByIdNotFound(id).left()
        }

    override suspend fun getShort(id: PointId, language: Language): Either<AbstractBackendException, PointShortModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(id)?.let{
                cityRepository.getShort(it.city.id.value, language).flatMap { city ->
                    pointDetailRepository.getDetailFor(id, language).flatMap { detail ->
                        PointShortModel(
                            id = id,
                            address = detail.address,
                            schedule = detail.schedule,
                            city = city
                        ).right()
                    }
                }
            } ?: pointByIdNotFound(id).left()
        }

    override suspend fun getByCity(cityId: CityId, language: Language): Either<AbstractBackendException, List<PointShortModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            cityRepository.getShort(cityId, language).flatMap { city ->
                Point.find { Points.city eq cityId }
                    .parTraverseEither {
                        pointDetailRepository.getDetailFor(it.id.value, language).flatMap { detail ->
                            PointShortModel(
                                id = detail.parentId,
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
                                        id = detail.parentId,
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

    override fun Point.Companion.new(createModel: PointCreateModel): Either<AbstractBackendException, Point> {
        return new {  }.right()
    }

    override fun Point.applyC(createModel: PointCreateModel): Either<AbstractBackendException, Point> {
        return apply {
            updateDate = UpdateTime.now()
        }.right()
    }

    override fun Point.applyU(updateModel: PointUpdateModel): Either<AbstractBackendException, Point> {
        return apply {
            updateDate = UpdateTime.now()
        }.right()
    }

}