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
import ru.novolmob.backendapi.exceptions.deviceTypeByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IDeviceTypeDetailRepository
import ru.novolmob.backendapi.repositories.IDeviceTypeRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.exposeddatabase.entities.DeviceType
import ru.novolmob.exposeddatabase.tables.DeviceTypes

class DeviceTypeRepositoryImpl(
    mapper: Mapper<DeviceType, DeviceTypeModel>,
    resultRowMapper: Mapper<ResultRow, DeviceTypeModel>,
    val deviceTypeDetailRepository: IDeviceTypeDetailRepository
): IDeviceTypeRepository, AbstractCrudRepository<DeviceTypeId, DeviceType.Companion, DeviceType, DeviceTypeModel, DeviceTypeCreateModel, DeviceTypeUpdateModel>(
    DeviceType.Companion, mapper, resultRowMapper, ::deviceTypeByIdNotFound
) {
    override suspend fun getFull(
        id: DeviceTypeId,
        language: Language
    ): Either<AbstractBackendException, DeviceTypeFullModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            DeviceType.findById(id)?.let {
                deviceTypeDetailRepository.getDetailFor(id, language).flatMap {
                    DeviceTypeFullModel(
                        id = id,
                        detail = it
                    ).right()
                }
            } ?: deviceTypeByIdNotFound(id).left()
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

    override fun DeviceType.Companion.new(createModel: DeviceTypeCreateModel): Either<AbstractBackendException, DeviceType> {
        return new {  }.right()
    }

    override fun DeviceType.applyC(createModel: DeviceTypeCreateModel): Either<AbstractBackendException, DeviceType> {
        return apply {
            this.updateDate = UpdateTime.now()
        }.right()
    }

    override fun DeviceType.applyU(updateModel: DeviceTypeUpdateModel): Either<AbstractBackendException, DeviceType> {
        return apply {
            this.updateDate = UpdateTime.now()
        }.right()
    }
}