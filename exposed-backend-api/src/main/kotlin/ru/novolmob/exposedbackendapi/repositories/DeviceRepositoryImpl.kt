package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IDeviceDetailRepository
import ru.novolmob.backendapi.repositories.IDeviceRepository
import ru.novolmob.backendapi.repositories.IDeviceTypeRepository
import ru.novolmob.backendapi.repositories.IPointToDeviceRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateDate
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound
import ru.novolmob.exposedbackendapi.exceptions.deviceTypeByIdNotFound
import ru.novolmob.exposeddatabase.entities.Device
import ru.novolmob.exposeddatabase.entities.DeviceType
import ru.novolmob.exposeddatabase.tables.Devices

class DeviceRepositoryImpl(
    val mapper: Mapper<Device, DeviceModel>,
    val resultRowMapper: Mapper<ResultRow, DeviceModel>,
    val deviceDetailRepository: IDeviceDetailRepository,
    val deviceTypeRepository: IDeviceTypeRepository,
    val pointToDeviceRepository: IPointToDeviceRepository
): IDeviceRepository {
    override suspend fun getFull(deviceId: DeviceId, language: Language): Either<BackendException, DeviceFullModel> =
        newSuspendedTransaction(Dispatchers.IO) { 
            Device.findById(deviceId)?.let {
                deviceDetailRepository.getDetailFor(deviceId, language).flatMap { detail ->
                    deviceTypeRepository.getFull(it.type.id.value, language).flatMap { type ->
                        pointToDeviceRepository.getPoints(deviceId, language).flatMap { points ->
                            DeviceFullModel(
                                id = deviceId,
                                article = it.article,
                                type = type,
                                detailModel = detail,
                                points = points,
                                price = it.price
                            ).right()
                        }
                    }
                }
            } ?: deviceByIdNotFound(deviceId).left()
        }

    override suspend fun get(id: DeviceId): Either<BackendException, DeviceModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Device.findById(id)?.let(mapper::invoke) ?: deviceByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<DeviceModel>> =
        RepositoryUtil.generalGatAll(Devices, pagination, resultRowMapper)

    override suspend fun post(createModel: DeviceCreateModel): Either<BackendException, DeviceModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val type = DeviceType.findById(createModel.typeId) ?: return@newSuspendedTransaction deviceTypeByIdNotFound(
                createModel.typeId
            ).left()
            Device.new {
                this.article = createModel.article
                this.type = type
                this.price = createModel.price
            }.let(mapper::invoke)
        }

    override suspend fun post(id: DeviceId, createModel: DeviceCreateModel): Either<BackendException, DeviceModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val type = DeviceType.findById(createModel.typeId) ?: return@newSuspendedTransaction deviceTypeByIdNotFound(
                createModel.typeId
            ).left()
            Device.findById(id)?.apply {
                this.article = createModel.article
                this.type = type
                this.price = createModel.price
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: deviceByIdNotFound(id).left()
        }

    override suspend fun put(id: DeviceId, updateModel: DeviceUpdateModel): Either<BackendException, DeviceModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val type = updateModel.typeId?.let {
                DeviceType.findById(it) ?: return@newSuspendedTransaction deviceTypeByIdNotFound(
                    it
                ).left()
            }
            Device.findById(id)?.apply {
                updateModel.article?.let { this.article = it }
                type?.let { this.type = it }
                updateModel.price?.let { this.price = it }
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: deviceByIdNotFound(id).left()
        }

    override suspend fun delete(id: DeviceId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) { 
            Device.findById(id)?.let { 
                it.delete()
                true.right()
            } ?: false.right()
        }
}