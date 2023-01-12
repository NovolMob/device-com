package ru.novolmob.exposedbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.deviceByIdNotFound
import ru.novolmob.backendapi.exceptions.deviceDetailByDeviceIdAndLanguageNotFound
import ru.novolmob.backendapi.exceptions.deviceDetailByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceDetailCreateModel
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.backendapi.models.DeviceDetailUpdateModel
import ru.novolmob.backendapi.repositories.IDeviceDetailRepository
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.exposeddatabase.entities.Device
import ru.novolmob.exposeddatabase.entities.details.DeviceDetail

class DeviceDetailRepositoryImpl(
    mapper: Mapper<DeviceDetail, DeviceDetailModel>,
    resultRowMapper: Mapper<ResultRow, DeviceDetailModel>,
): IDeviceDetailRepository,
    AbstractDetailRepository<DeviceDetailId, DeviceDetail.Companion, DeviceDetail, Device, DeviceId, DeviceDetailModel, DeviceDetailCreateModel, DeviceDetailUpdateModel>(
        DeviceDetail.Companion, mapper, resultRowMapper, ::deviceDetailByIdNotFound, ::deviceDetailByDeviceIdAndLanguageNotFound
    ) {

    override fun DeviceDetail.Companion.new(createModel: DeviceDetailCreateModel): Either<AbstractBackendException, DeviceDetail> {
        val device = Device.findById(createModel.deviceId)
            ?: return deviceByIdNotFound(createModel.deviceId).left()
        return new {
            this.parent = device
            this.title = createModel.title
            this.description = createModel.description
            this.features = createModel.features
            this.language = createModel.language
        }.right()
    }

    override fun DeviceDetail.applyC(createModel: DeviceDetailCreateModel): Either<AbstractBackendException, DeviceDetail> {
        val device = Device.findById(createModel.deviceId)
            ?: return deviceByIdNotFound(createModel.deviceId).left()
        return apply {
            this.parent = device
            this.title = createModel.title
            this.description = createModel.description
            this.features = createModel.features
            this.language = createModel.language
            this.updateDate = UpdateTime.now()
        }.right()
    }

    override fun DeviceDetail.applyU(updateModel: DeviceDetailUpdateModel): Either<AbstractBackendException, DeviceDetail> {
        val device = updateModel.deviceId?.let {
            Device.findById(it) ?: return deviceByIdNotFound(it).left()
        }
        return apply {
            device?.let { this.parent = it }
            updateModel.title?.let { this.title = it }
            updateModel.description?.let { this.description = it }
            updateModel.features?.let { this.features = it }
            updateModel.language?.let { this.language = it }
            this.updateDate = UpdateTime.now()
        }.right()
    }
}