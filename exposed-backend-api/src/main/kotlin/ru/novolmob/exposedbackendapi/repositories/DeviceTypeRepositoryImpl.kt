package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IDeviceTypeDetailRepository
import ru.novolmob.backendapi.repositories.IDeviceTypeRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateDate
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
    ): Either<BackendException, DeviceTypeFullModel> =
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
    ): Either<BackendException, Page<DeviceTypeShortModel>> =
        RepositoryUtil.generalGatAll(DeviceTypes, pagination, resultRowMapper).flatMap { page ->
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

    override suspend fun get(id: DeviceTypeId): Either<BackendException, DeviceTypeModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.findById(id)?.let(mapper::invoke) ?: deviceTypeByIdNotFound(
                id
            ).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<DeviceTypeModel>> =
        RepositoryUtil.generalGatAll(DeviceTypes, pagination, resultRowMapper)

    override suspend fun post(createModel: DeviceTypeCreateModel): Either<BackendException, DeviceTypeModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.new {  }.let(mapper::invoke)
        }

    override suspend fun post(
        id: DeviceTypeId,
        createModel: DeviceTypeCreateModel
    ): Either<BackendException, DeviceTypeModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.findById(id)?.apply {
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke)
                ?: deviceTypeByIdNotFound(id).left()
        }

    override suspend fun put(
        id: DeviceTypeId,
        updateModel: DeviceTypeUpdateModel
    ): Either<BackendException, DeviceTypeModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.findById(id)?.apply {
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke)
                ?: deviceTypeByIdNotFound(id).left()
        }

    override suspend fun delete(id: DeviceTypeId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}