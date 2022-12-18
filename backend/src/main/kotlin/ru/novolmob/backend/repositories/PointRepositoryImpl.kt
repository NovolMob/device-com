package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.pointByIdNotFound
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IPointDetailRepository
import ru.novolmob.backendapi.repositories.IPointRepository
import ru.novolmob.exposeddatabase.entities.Point
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.exposeddatabase.tables.Points

class PointRepositoryImpl(
    val mapper: Mapper<Point, PointModel>,
    val resultRowMapper: Mapper<ResultRow, PointModel>,
    val pointDetailRepository: IPointDetailRepository
): IPointRepository {
    override suspend fun getFull(pointId: PointId, language: Language): Either<BackendException, PointFullModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(pointId)?.let{
                pointDetailRepository.getDetailFor(pointId, language).flatMap { detail ->
                    PointFullModel(
                        id = pointId,
                        detail = detail
                    ).right()
                }
            } ?: pointByIdNotFound(pointId).left()
        }

    override suspend fun get(id: PointId): Either<BackendException, PointModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(id)?.let(mapper::invoke) ?: pointByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<PointModel>> =
        RepositoryUtil.generalGatAll(Points, pagination, resultRowMapper)

    override suspend fun post(createModel: PointCreateModel): Either<BackendException, PointModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.new {

            }.let(mapper::invoke)
        }

    override suspend fun post(id: PointId, createModel: PointCreateModel): Either<BackendException, PointModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(id)?.apply {

            }?.let(mapper::invoke) ?: pointByIdNotFound(id).left()
        }

    override suspend fun put(id: PointId, updateModel: PointUpdateModel): Either<BackendException, PointModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(id)?.apply {

            }?.let(mapper::invoke) ?: pointByIdNotFound(id).left()
        }

    override suspend fun delete(id: PointId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Point.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}