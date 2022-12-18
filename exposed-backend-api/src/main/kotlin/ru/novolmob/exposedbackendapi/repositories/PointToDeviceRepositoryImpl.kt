package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.exposedbackendapi.exceptions.pointByIdNotFound
import ru.novolmob.exposedbackendapi.exceptions.pointToDeviceEntityByIdNotFound
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IPointDetailRepository
import ru.novolmob.backendapi.repositories.IPointToDeviceRepository
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateDate
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.PointToDeviceEntityId
import ru.novolmob.exposeddatabase.entities.Device
import ru.novolmob.exposeddatabase.entities.Point
import ru.novolmob.exposeddatabase.entities.PointToDeviceEntity
import ru.novolmob.exposeddatabase.tables.PointToDeviceTable

class PointToDeviceRepositoryImpl(
    val mapper: Mapper<PointToDeviceEntity, PointToDeviceEntityModel>,
    val itemMapper: Mapper<PointToDeviceEntity, PointItemModel>,
    val resultRowMapper: Mapper<ResultRow, PointToDeviceEntityModel>,
    val pointDetailRepository: IPointDetailRepository
): IPointToDeviceRepository {

    private fun find(pointId: PointId, deviceId: DeviceId): PointToDeviceEntity? =
        PointToDeviceEntity.find { (PointToDeviceTable.point eq pointId) and (PointToDeviceTable.device eq deviceId) }
            .limit(1).firstOrNull()
    override suspend fun getDevices(pointId: PointId): Either<BackendException, List<PointItemModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            PointToDeviceEntity.find { PointToDeviceTable.point eq pointId }
                .parTraverseEither { itemMapper(it) }
        }

    override suspend fun getPoints(deviceId: DeviceId, language: Language): Either<BackendException, List<NumberOfDeviceInPointModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            PointToDeviceEntity.find { PointToDeviceTable.device eq deviceId }
                .parTraverseEither { entity ->
                    pointDetailRepository.getDetailFor(entity.point.id.value, language).flatMap { detail ->
                        NumberOfDeviceInPointModel(
                            pointId = detail.pointId,
                            pointDetail = detail,
                            amount = entity.amount
                        ).right()
                    }
                }
        }

    override suspend fun setInPoint(
        pointId: PointId,
        deviceId: DeviceId,
        amount: Amount
    ): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            val pointToDevice = find(pointId, deviceId)
            if (amount.int > 0) {
                pointToDevice?.run {
                    this.amount = amount
                    this.updateDate = UpdateDate.now()
                    true.right()
                } ?: let {
                    post(PointToDeviceEntityCreateModel(pointId, deviceId, amount)).flatMap {
                        false.right()
                    }
                }
            } else if (pointToDevice != null) {
                removeFromPoint(pointId, deviceId)
            } else false.right()
        }

    override suspend fun removeFromPoint(pointId: PointId, deviceId: DeviceId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            find(pointId, deviceId)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

    override suspend fun getNumberOfDevicesInPoint(
        pointId: PointId,
        deviceId: DeviceId
    ): Either<BackendException, Amount> =
        newSuspendedTransaction(Dispatchers.IO) {
            (find(pointId, deviceId)?.amount ?: Amount(0)).right()
        }

    override suspend fun get(id: PointToDeviceEntityId): Either<BackendException, PointToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            PointToDeviceEntity.findById(id)?.let(mapper::invoke) ?: pointToDeviceEntityByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<PointToDeviceEntityModel>> =
        RepositoryUtil.generalGatAll(PointToDeviceTable, pagination, resultRowMapper)

    override suspend fun post(createModel: PointToDeviceEntityCreateModel): Either<BackendException, PointToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = Point.findById(createModel.pointId) ?: return@newSuspendedTransaction pointByIdNotFound(createModel.pointId).left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound(
                createModel.deviceId
            ).left()
            PointToDeviceEntity.new {
                this.point = point
                this.device = device
                this.amount = createModel.amount
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: PointToDeviceEntityId,
        createModel: PointToDeviceEntityCreateModel
    ): Either<BackendException, PointToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = Point.findById(createModel.pointId) ?: return@newSuspendedTransaction pointByIdNotFound(createModel.pointId).left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound(
                createModel.deviceId
            ).left()
            PointToDeviceEntity.findById(id)?.apply {
                this.point = point
                this.device = device
                this.amount = createModel.amount
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: pointToDeviceEntityByIdNotFound(id).left()
        }

    override suspend fun put(
        id: PointToDeviceEntityId,
        updateModel: PointToDeviceEntityUpdateModel
    ): Either<BackendException, PointToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val point = updateModel.pointId?.let {
                Point.findById(it) ?: return@newSuspendedTransaction pointByIdNotFound(it).left()
            }
            val device = updateModel.deviceId?.let {
                Device.findById(it) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound(
                    it
                ).left()
            }
            PointToDeviceEntity.findById(id)?.apply {
                point?.let { this.point = it }
                device?.let { this.device = it }
                updateModel.amount?.let { this.amount = it }
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: pointToDeviceEntityByIdNotFound(id).left()
        }

    override suspend fun delete(id: PointToDeviceEntityId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            PointToDeviceEntity.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}