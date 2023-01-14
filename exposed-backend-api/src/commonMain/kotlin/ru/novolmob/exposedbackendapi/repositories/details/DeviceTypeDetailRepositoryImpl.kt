package ru.novolmob.exposedbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.deviceTypeByIdNotFound
import ru.novolmob.backendapi.exceptions.deviceTypeDetailByDeviceIdAndLanguageNotFound
import ru.novolmob.backendapi.exceptions.deviceTypeDetailByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceTypeDetailCreateModel
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.backendapi.models.DeviceTypeDetailUpdateModel
import ru.novolmob.backendapi.repositories.IDeviceTypeDetailRepository
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.exposeddatabase.entities.DeviceType
import ru.novolmob.exposeddatabase.entities.details.DeviceTypeDetail

class DeviceTypeDetailRepositoryImpl(
    mapper: Mapper<DeviceTypeDetail, DeviceTypeDetailModel>,
    resultRowMapper: Mapper<ResultRow, DeviceTypeDetailModel>
): IDeviceTypeDetailRepository,
    AbstractDetailRepository<DeviceTypeDetailId, DeviceTypeDetail.Companion, DeviceTypeDetail, DeviceType, DeviceTypeId, DeviceTypeDetailModel, DeviceTypeDetailCreateModel, DeviceTypeDetailUpdateModel>(
        DeviceTypeDetail.Companion, mapper, resultRowMapper, ::deviceTypeDetailByIdNotFound, ::deviceTypeDetailByDeviceIdAndLanguageNotFound
    ) {

    override suspend fun post(createModel: DeviceTypeDetailCreateModel): Either<AbstractBackendException, DeviceTypeDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val deviceType = DeviceType.findById(createModel.deviceTypeId)
                ?: return@newSuspendedTransaction deviceTypeByIdNotFound(createModel.deviceTypeId).left()
            DeviceTypeDetail.new {
                this.parent = deviceType
                this.title = createModel.title
                this.description = createModel.description
                this.language = createModel.language
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: DeviceTypeDetailId,
        createModel: DeviceTypeDetailCreateModel
    ): Either<AbstractBackendException, DeviceTypeDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val deviceType = DeviceType.findById(createModel.deviceTypeId)
                ?: return@newSuspendedTransaction deviceTypeByIdNotFound(createModel.deviceTypeId).left()
            DeviceTypeDetail.findById(id)?.apply {
                this.parent = deviceType
                this.title = createModel.title
                this.description = createModel.description
                this.language = createModel.language
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke) ?: deviceTypeDetailByIdNotFound(id).left()
        }

    override suspend fun put(
        id: DeviceTypeDetailId,
        updateModel: DeviceTypeDetailUpdateModel
    ): Either<AbstractBackendException, DeviceTypeDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val deviceType = updateModel.deviceTypeId?.let {
                DeviceType.findById(it) ?: return@newSuspendedTransaction deviceTypeByIdNotFound(it).left()
            }
            DeviceTypeDetail.findById(id)?.apply {
                deviceType?.let { this.parent = it }
                updateModel.title?.let { this.title = it }
                updateModel.description?.let { this.description = it }
                updateModel.language?.let { this.language = it }
                this.updateDate = UpdateTime.now()

            }?.let(mapper::invoke) ?: deviceTypeDetailByIdNotFound(id).left()
        }

    override fun DeviceTypeDetail.Companion.new(createModel: DeviceTypeDetailCreateModel): Either<AbstractBackendException, DeviceTypeDetail> {
        val deviceType = DeviceType.findById(createModel.deviceTypeId)
            ?: return deviceTypeByIdNotFound(createModel.deviceTypeId).left()
        return new {
            this.parent = deviceType
            this.title = createModel.title
            this.description = createModel.description
            this.language = createModel.language
        }.right()
    }

    override fun DeviceTypeDetail.applyC(createModel: DeviceTypeDetailCreateModel): Either<AbstractBackendException, DeviceTypeDetail> {
        val deviceType = DeviceType.findById(createModel.deviceTypeId)
            ?: return deviceTypeByIdNotFound(createModel.deviceTypeId).left()
        return apply {
            this.parent = deviceType
            this.title = createModel.title
            this.description = createModel.description
            this.language = createModel.language
            this.updateDate = UpdateTime.now()
        }.right()
    }

    override fun DeviceTypeDetail.applyU(updateModel: DeviceTypeDetailUpdateModel): Either<AbstractBackendException, DeviceTypeDetail> {
        val deviceType = updateModel.deviceTypeId?.let {
            DeviceType.findById(it) ?: return deviceTypeByIdNotFound(it).left()
        }
        return apply {
            deviceType?.let { this.parent = it }
            updateModel.title?.let { this.title = it }
            updateModel.description?.let { this.description = it }
            updateModel.language?.let { this.language = it }
            this.updateDate = UpdateTime.now()
        }.right()
    }

}