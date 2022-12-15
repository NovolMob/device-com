package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.deviceTypeByIdNotFound
import ru.novolmob.backend.exceptions.deviceTypeDetailByDeviceIdAndLanguageNotFound
import ru.novolmob.backend.exceptions.deviceTypeDetailByIdNotFound
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IDeviceTypeDetailRepository
import ru.novolmob.database.entities.DeviceType
import ru.novolmob.database.entities.DeviceTypeDetail
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.UpdateDate
import ru.novolmob.database.models.ids.DeviceTypeDetailId
import ru.novolmob.database.models.ids.DeviceTypeId
import ru.novolmob.database.tables.DeviceTypeDetails

class DeviceTypeDetailRepositoryImpl(
    val mapper: Mapper<DeviceTypeDetail, DeviceTypeDetailModel>,
    val resultRowMapper: Mapper<ResultRow, DeviceTypeDetailModel>
): IDeviceTypeDetailRepository {
    override suspend fun getDetailFor(
        deviceTypeId: DeviceTypeId,
        language: Language
    ): Either<BackendException, DeviceTypeDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceTypeDetail
                .find { (DeviceTypeDetails.deviceType eq deviceTypeId) and (DeviceTypeDetails.language eq language) }
                .limit(1).firstOrNull()
                ?.let(mapper::invoke) ?: deviceTypeDetailByDeviceIdAndLanguageNotFound(deviceTypeId, language).left()
        }

    override suspend fun removeDetailFor(deviceTypeId: DeviceTypeId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceTypeDetail.find { DeviceTypeDetails.deviceType eq deviceTypeId }
                .takeIf { !it.empty() }
                ?.let {
                    it.parTraverse { it.delete() }
                    true.right()
                } ?: false.right()
        }

    override suspend fun get(id: DeviceTypeDetailId): Either<BackendException, DeviceTypeDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceTypeDetail.findById(id)?.let(mapper::invoke) ?: deviceTypeDetailByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<DeviceTypeDetailModel>> =
        RepositoryUtil.generalGatAll(DeviceTypeDetails, pagination, resultRowMapper)

    override suspend fun post(createModel: DeviceTypeDetailCreateModel): Either<BackendException, DeviceTypeDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val deviceType = DeviceType.findById(createModel.deviceTypeId) ?: return@newSuspendedTransaction deviceTypeByIdNotFound(createModel.deviceTypeId).left()
            DeviceTypeDetail.new {
                this.deviceType = deviceType
                this.title = createModel.title
                this.description = createModel.description
                this.language = createModel.language
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: DeviceTypeDetailId,
        createModel: DeviceTypeDetailCreateModel
    ): Either<BackendException, DeviceTypeDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val deviceType = DeviceType.findById(createModel.deviceTypeId) ?: return@newSuspendedTransaction deviceTypeByIdNotFound(createModel.deviceTypeId).left()
            DeviceTypeDetail.findById(id)?.apply {
                this.deviceType = deviceType
                this.title = createModel.title
                this.description = createModel.description
                this.language = createModel.language
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: deviceTypeDetailByIdNotFound(id).left()
        }

    override suspend fun put(
        id: DeviceTypeDetailId,
        updateModel: DeviceTypeDetailUpdateModel
    ): Either<BackendException, DeviceTypeDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val deviceType = updateModel.deviceTypeId?.let {
                DeviceType.findById(it) ?: return@newSuspendedTransaction deviceTypeByIdNotFound(it).left()
            }
            DeviceTypeDetail.findById(id)?.apply {
                deviceType?.let { this.deviceType = it }
                updateModel.title?.let { this.title = it }
                updateModel.description?.let { this.description = it }
                updateModel.language?.let { this.language = it }
                this.updateDate = UpdateDate.now()

            }?.let(mapper::invoke) ?: deviceTypeDetailByIdNotFound(id).left()
        }

    override suspend fun delete(id: DeviceTypeDetailId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceTypeDetail.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}