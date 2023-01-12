package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.deviceByIdNotFound
import ru.novolmob.backendapi.exceptions.deviceTypeByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceCreateModel
import ru.novolmob.backendapi.models.DeviceFullModel
import ru.novolmob.backendapi.models.DeviceModel
import ru.novolmob.backendapi.models.DeviceUpdateModel
import ru.novolmob.backendapi.repositories.IDeviceDetailRepository
import ru.novolmob.backendapi.repositories.IDeviceRepository
import ru.novolmob.backendapi.repositories.IDeviceTypeRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.exposeddatabase.entities.Device
import ru.novolmob.exposeddatabase.entities.DeviceType

class DeviceRepositoryImpl(
    mapper: Mapper<Device, DeviceModel>,
    resultRowMapper: Mapper<ResultRow, DeviceModel>,
    val deviceDetailRepository: IDeviceDetailRepository,
    val deviceTypeRepository: IDeviceTypeRepository
): IDeviceRepository, AbstractCrudRepository<DeviceId, Device.Companion, Device, DeviceModel, DeviceCreateModel, DeviceUpdateModel>(
    Device.Companion, mapper, resultRowMapper, ::deviceByIdNotFound
) {
    override suspend fun getFull(id: DeviceId, language: Language): Either<AbstractBackendException, DeviceFullModel> =
        newSuspendedTransaction(Dispatchers.IO) { 
            Device.findById(id)?.let {
                deviceDetailRepository.getDetailFor(id, language).flatMap { detail ->
                    deviceTypeRepository.getFull(it.type.id.value, language).flatMap { type ->
                        DeviceFullModel(
                            id = id,
                            article = it.article,
                            type = type,
                            detailModel = detail,
                            price = it.price,
                            amount = it.amount
                        ).right()
                    }
                }
            } ?: deviceByIdNotFound(id).left()
        }

    override fun Device.Companion.new(createModel: DeviceCreateModel): Either<AbstractBackendException, Device> {
        val type = DeviceType.findById(createModel.typeId)
            ?: return deviceTypeByIdNotFound(createModel.typeId).left()
        return new {
            this.article = createModel.article
            this.type = type
            this.price = createModel.price
        }.right()
    }

    override fun Device.applyC(createModel: DeviceCreateModel): Either<AbstractBackendException, Device> {
        val type = DeviceType.findById(createModel.typeId)
            ?: return deviceTypeByIdNotFound(createModel.typeId).left()
        return apply {
            this.article = createModel.article
            this.type = type
            this.price = createModel.price
            this.updateDate = UpdateTime.now()
        }.right()
    }

    override fun Device.applyU(updateModel: DeviceUpdateModel): Either<AbstractBackendException, Device> {
        val type = updateModel.typeId?.let {
            DeviceType.findById(it) ?: return deviceTypeByIdNotFound(it).left()
        }
        return apply {
            updateModel.article?.let { this.article = it }
            type?.let { this.type = it }
            updateModel.price?.let { this.price = it }
            this.updateDate = UpdateTime.now()
        }.right()
    }
}