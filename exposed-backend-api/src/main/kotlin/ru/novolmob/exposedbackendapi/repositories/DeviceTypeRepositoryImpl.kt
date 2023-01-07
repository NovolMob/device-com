package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IDeviceTypeDetailRepository
import ru.novolmob.backendapi.repositories.IDeviceTypeRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.exposedbackendapi.exceptions.deviceTypeByIdNotFound
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.exposeddatabase.entities.DeviceType
import ru.novolmob.exposeddatabase.tables.DeviceTypes

class DeviceTypeRepositoryImpl(
    val mapper: Mapper<DeviceType, DeviceTypeModel>,
    val resultRowMapper: Mapper<ResultRow, DeviceTypeModel>,
    val deviceTypeDetailRepository: IDeviceTypeDetailRepository
): IDeviceTypeRepository {
    override suspend fun getFull(
        deviceTypeId: DeviceTypeId,
        language: Language
    ): Either<AbstractBackendException, DeviceTypeFullModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.findById(deviceTypeId)?.let {
                deviceTypeDetailRepository.getDetailFor(deviceTypeId, language).flatMap {
                    DeviceTypeFullModel(
                        id = deviceTypeId,
                        detail = it
                    ).right()
                }
            } ?: deviceTypeByIdNotFound(deviceTypeId).left()
        }

    override suspend fun getAll(
        pagination: Pagination,
        language: Language
    ): Either<AbstractBackendException, Page<DeviceTypeShortModel>> =
        RepositoryUtil.generalGetAll(DeviceTypes, pagination, resultRowMapper).flatMap { page ->
            page.list.parTraverseEither {
                deviceTypeDetailRepository.getDetailFor(it.id, language).flatMap { typeDetail ->
                    DeviceTypeShortModel(
                        id = typeDetail.deviceTypeId,
                        title = typeDetail.title
                    ).right()
                }
            }.flatMap {
                Page(page.page, page.size, list = it).right()
            }
        }

    override suspend fun get(id: DeviceTypeId): Either<AbstractBackendException, DeviceTypeModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.findById(id)?.let(mapper::invoke) ?: deviceTypeByIdNotFound(
                id
            ).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<DeviceTypeModel>> =
        RepositoryUtil.generalGetAll(DeviceTypes, pagination, resultRowMapper)

    override suspend fun post(createModel: DeviceTypeCreateModel): Either<AbstractBackendException, DeviceTypeModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.new {  }.let(mapper::invoke)
        }

    override suspend fun post(
        id: DeviceTypeId,
        createModel: DeviceTypeCreateModel
    ): Either<AbstractBackendException, DeviceTypeModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.findById(id)?.apply {
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke)
                ?: deviceTypeByIdNotFound(id).left()
        }

    override suspend fun put(
        id: DeviceTypeId,
        updateModel: DeviceTypeUpdateModel
    ): Either<AbstractBackendException, DeviceTypeModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.findById(id)?.apply {
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke)
                ?: deviceTypeByIdNotFound(id).left()
        }

    override suspend fun delete(id: DeviceTypeId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}