package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.exposedbackendapi.exceptions.pointByIdNotFound
import ru.novolmob.exposedbackendapi.exceptions.pointDetailByIdNotFound
import ru.novolmob.exposedbackendapi.exceptions.pointDetailByPointIdAndLanguageNotFound
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IPointDetailRepository
import ru.novolmob.exposeddatabase.entities.Point
import ru.novolmob.exposeddatabase.entities.PointDetail
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.exposeddatabase.tables.PointDetails

class PointDetailRepositoryImpl(
    val mapper: Mapper<PointDetail, PointDetailModel>,
    val resultRowMapper: Mapper<ResultRow, PointDetailModel>,
): IPointDetailRepository {
    override suspend fun getDetailFor(
        pointId: PointId,
        language: Language
    ): Either<AbstractBackendException, PointDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            PointDetail.find { (PointDetails.point eq pointId) and (PointDetails.language eq language) }
                .limit(1).firstOrNull()?.let(mapper::invoke) ?: pointDetailByPointIdAndLanguageNotFound(pointId, language).left()
        }

    override suspend fun removeFor(pointId: PointId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            PointDetail.find { PointDetails.point eq pointId }
                .takeIf { !it.empty() }?.let {
                    it.parTraverse { it.delete() }
                    true.right()
                } ?: false.right()
        }

    override suspend fun get(id: PointDetailId): Either<AbstractBackendException, PointDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            PointDetail.findById(id)?.let(mapper::invoke) ?: pointDetailByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<PointDetailModel>> =
        RepositoryUtil.generalGetAll(PointDetails, pagination, resultRowMapper)

    override suspend fun post(createModel: PointDetailCreateModel): Either<AbstractBackendException, PointDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = Point.findById(createModel.pointId) ?: return@newSuspendedTransaction pointByIdNotFound(createModel.pointId).left()
            PointDetail.new {
                this.point = point
                this.address = createModel.address
                this.schedule = createModel.schedule
                this.description = createModel.description
                this.language = createModel.language
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: PointDetailId,
        createModel: PointDetailCreateModel
    ): Either<AbstractBackendException, PointDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = Point.findById(createModel.pointId) ?: return@newSuspendedTransaction pointByIdNotFound(createModel.pointId).left()
            PointDetail.findById(id)?.apply {
                this.point = point
                this.address = createModel.address
                this.schedule = createModel.schedule
                this.description = createModel.description
                this.language = createModel.language
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke) ?: pointDetailByIdNotFound(id).left()
        }

    override suspend fun put(
        id: PointDetailId,
        updateModel: PointDetailUpdateModel
    ): Either<AbstractBackendException, PointDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = updateModel.pointId?.let {
                Point.findById(it) ?: return@newSuspendedTransaction pointByIdNotFound(it).left()
            }
            PointDetail.findById(id)?.apply {
                point?.let { this.point = it }
                updateModel.address?.let { this.address = it }
                updateModel.schedule?.let { this.schedule = it }
                updateModel.description?.let { this.description = it }
                updateModel.language?.let { this.language = it }
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke) ?: pointDetailByIdNotFound(id).left()
        }

    override suspend fun delete(id: PointDetailId): Either<AbstractBackendException, Boolean> =
        PointDetail.findById(id)?.let {
            it.delete()
            true.right()
        } ?: false.right()
}