package ru.novolmob.exposedbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.pointByIdNotFound
import ru.novolmob.backendapi.exceptions.pointDetailByIdNotFound
import ru.novolmob.backendapi.exceptions.pointDetailByPointIdAndLanguageNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.PointDetailCreateModel
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.backendapi.models.PointDetailUpdateModel
import ru.novolmob.backendapi.repositories.IPointDetailRepository
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.exposeddatabase.entities.Point
import ru.novolmob.exposeddatabase.entities.details.PointDetail

class PointDetailRepositoryImpl(
    mapper: Mapper<PointDetail, PointDetailModel>,
    resultRowMapper: Mapper<ResultRow, PointDetailModel>,
): IPointDetailRepository,
    AbstractDetailRepository<PointDetailId, PointDetail.Companion, PointDetail, Point, PointId, PointDetailModel, PointDetailCreateModel, PointDetailUpdateModel>(
        PointDetail.Companion, mapper, resultRowMapper, ::pointDetailByIdNotFound, ::pointDetailByPointIdAndLanguageNotFound
    ) {

    override suspend fun post(createModel: PointDetailCreateModel): Either<AbstractBackendException, PointDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = Point.findById(createModel.pointId) ?: return@newSuspendedTransaction pointByIdNotFound(createModel.pointId).left()
            PointDetail.new {
                this.parent = point
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
                this.parent = point
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
                point?.let { this.parent = it }
                updateModel.address?.let { this.address = it }
                updateModel.schedule?.let { this.schedule = it }
                updateModel.description?.let { this.description = it }
                updateModel.language?.let { this.language = it }
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke) ?: pointDetailByIdNotFound(id).left()
        }

    override fun PointDetail.Companion.new(createModel: PointDetailCreateModel): Either<AbstractBackendException, PointDetail> {
        val point = Point.findById(createModel.pointId) ?: return pointByIdNotFound(createModel.pointId).left()
        return new {
            this.parent = point
            this.address = createModel.address
            this.schedule = createModel.schedule
            this.description = createModel.description
            this.language = createModel.language
        }.right()
    }

    override fun PointDetail.applyC(createModel: PointDetailCreateModel): Either<AbstractBackendException, PointDetail> {
        val point = Point.findById(createModel.pointId) ?: return pointByIdNotFound(createModel.pointId).left()
        return apply {
            this.parent = point
            this.address = createModel.address
            this.schedule = createModel.schedule
            this.description = createModel.description
            this.language = createModel.language
            this.updateDate = UpdateTime.now()
        }.right()
    }

    override fun PointDetail.applyU(updateModel: PointDetailUpdateModel): Either<AbstractBackendException, PointDetail> {
        val point = updateModel.pointId?.let {
            Point.findById(it) ?: return pointByIdNotFound(it).left()
        }
        return apply {
            point?.let { this.parent = it }
            updateModel.address?.let { this.address = it }
            updateModel.schedule?.let { this.schedule = it }
            updateModel.description?.let { this.description = it }
            updateModel.language?.let { this.language = it }
            this.updateDate = UpdateTime.now()
        }.right()
    }

}