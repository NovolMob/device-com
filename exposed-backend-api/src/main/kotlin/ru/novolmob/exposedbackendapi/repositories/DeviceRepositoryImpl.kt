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
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IDeviceDetailRepository
import ru.novolmob.backendapi.repositories.IDeviceRepository
import ru.novolmob.backendapi.repositories.IDeviceTypeRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateTime
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
    val deviceTypeRepository: IDeviceTypeRepository
): IDeviceRepository {
    override suspend fun getFull(deviceId: DeviceId, language: Language): Either<AbstractBackendException, DeviceFullModel> =
        newSuspendedTransaction(Dispatchers.IO) { 
            Device.findById(deviceId)?.let {
                deviceDetailRepository.getDetailFor(deviceId, language).flatMap { detail ->
                    deviceTypeRepository.getFull(it.type.id.value, language).flatMap { type ->
                        DeviceFullModel(
                            id = deviceId,
                            article = it.article,
                            type = type,
                            detailModel = detail,
                            price = it.price,
                            amount = it.amount
                        ).right()
                    }
                }
            } ?: deviceByIdNotFound(deviceId).left()
        }

    override suspend fun get(id: DeviceId): Either<AbstractBackendException, DeviceModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Device.findById(id)?.let(mapper::invoke) ?: deviceByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<DeviceModel>> =
        RepositoryUtil.generalGetAll(Devices, pagination, resultRowMapper)

    override suspend fun post(createModel: DeviceCreateModel): Either<AbstractBackendException, DeviceModel> =
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

    override suspend fun post(id: DeviceId, createModel: DeviceCreateModel): Either<AbstractBackendException, DeviceModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val type = DeviceType.findById(createModel.typeId) ?: return@newSuspendedTransaction deviceTypeByIdNotFound(
                createModel.typeId
            ).left()
            Device.findById(id)?.apply {
                this.article = createModel.article
                this.type = type
                this.price = createModel.price
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke) ?: deviceByIdNotFound(id).left()
        }

    override suspend fun put(id: DeviceId, updateModel: DeviceUpdateModel): Either<AbstractBackendException, DeviceModel> =
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
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke) ?: deviceByIdNotFound(id).left()
        }

    override suspend fun delete(id: DeviceId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) { 
            Device.findById(id)?.let { 
                it.delete()
                true.right()
            } ?: false.right()
        }
}