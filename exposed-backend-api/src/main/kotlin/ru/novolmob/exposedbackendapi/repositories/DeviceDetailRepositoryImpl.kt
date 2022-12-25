package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IDeviceDetailRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound
import ru.novolmob.exposedbackendapi.exceptions.deviceDetailByDeviceIdAndLanguageNotFound
import ru.novolmob.exposedbackendapi.exceptions.deviceDetailByIdNotFound
import ru.novolmob.exposeddatabase.entities.Device
import ru.novolmob.exposeddatabase.entities.DeviceDetail
import ru.novolmob.exposeddatabase.tables.DeviceDetails

class DeviceDetailRepositoryImpl(
    val mapper: Mapper<DeviceDetail, DeviceDetailModel>,
    val resultRowMapper: Mapper<ResultRow, DeviceDetailModel>,
): IDeviceDetailRepository {
    override suspend fun getDetailFor(
        deviceId: DeviceId,
        language: Language
    ): Either<BackendException, DeviceDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceDetail.find { (DeviceDetails.device eq deviceId) and (DeviceDetails.language eq language) }
                .limit(1).firstOrNull()
                ?.let(mapper::invoke) ?: deviceDetailByDeviceIdAndLanguageNotFound(deviceId, language).left()
        }

    override suspend fun removeDetailFor(deviceId: DeviceId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceDetail.find { DeviceDetails.device eq deviceId }
                .takeIf { !it.empty() }
                ?.let {
                    it.parTraverse { it.delete() }
                    true.right()
                } ?: false.right()
        }

    override suspend fun get(id: DeviceDetailId): Either<BackendException, DeviceDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceDetail.findById(id)?.let(mapper::invoke) ?: deviceDetailByIdNotFound(
                id
            ).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<DeviceDetailModel>> =
        RepositoryUtil.generalGatAll(DeviceDetails, pagination, resultRowMapper)

    override suspend fun post(createModel: DeviceDetailCreateModel): Either<BackendException, DeviceDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction deviceByIdNotFound(
                createModel.deviceId
            ).left()
            DeviceDetail.new {
                this.device = device
                this.title = createModel.title
                this.description = createModel.description
                this.features = createModel.features
                this.language = createModel.language
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: DeviceDetailId,
        createModel: DeviceDetailCreateModel
    ): Either<BackendException, DeviceDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction deviceByIdNotFound(
                createModel.deviceId
            ).left()
            DeviceDetail.findById(id)?.apply {
                this.device = device
                this.title = createModel.title
                this.description = createModel.description
                this.features = createModel.features
                this.language = createModel.language
                updateDate = UpdateTime.now()
            }?.let(mapper::invoke) ?: deviceDetailByIdNotFound(id).left()
        }

    override suspend fun put(
        id: DeviceDetailId,
        updateModel: DeviceDetailUpdateModel
    ): Either<BackendException, DeviceDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val device = updateModel.deviceId?.let {
                Device.findById(it) ?: return@newSuspendedTransaction deviceByIdNotFound(
                    it
                ).left()
            }
            DeviceDetail.findById(id)?.apply {
                device?.let { this.device = it }
                updateModel.title?.let { this.title = it }
                updateModel.description?.let { this.description = it }
                updateModel.features?.let { this.features = it }
                updateModel.language?.let { this.language = it }
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke) ?: deviceDetailByIdNotFound(id).left()
        }

    override suspend fun delete(id: DeviceDetailId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceDetail.findById(id)?.let {
                it.device
                true.right()
            } ?: false.right()
        }
}