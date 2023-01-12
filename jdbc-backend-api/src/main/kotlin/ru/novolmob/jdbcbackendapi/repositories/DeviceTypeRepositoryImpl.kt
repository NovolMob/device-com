package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.deviceTypeByIdNotFound
import ru.novolmob.backendapi.exceptions.failedToCreateDeviceType
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IDeviceTypeRepository
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcbackendapi.utils.RepositoryUtil
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.tables.DeviceTypes
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet
import java.util.*

class DeviceTypeRepositoryImpl(
    val mapper: Mapper<ResultSet, DeviceTypeModel>,
    val shortModelMapper: Mapper<ResultSet, DeviceTypeShortModel>,
    val fullModelMapper: Mapper<ResultSet, DeviceTypeFullModel>
): IDeviceTypeRepository {
    override suspend fun getFull(
        id: DeviceTypeId,
        language: Language
    ): Either<AbstractBackendException, DeviceTypeFullModel> =
        DetailView.DeviceTypeDetailView.select(id, language) { fold(ifEmpty = { failedToCreateDeviceType() }, fullModelMapper::invoke) }

    override suspend fun getAll(
        pagination: Pagination,
        language: Language
    ): Either<AbstractBackendException, Page<DeviceTypeShortModel>> =
        RepositoryUtil.getAll(DetailView.DeviceTypeDetailView, pagination, language, shortModelMapper)

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<DeviceTypeModel>> =
        RepositoryUtil.getAll(DeviceTypes, pagination, mapper)

    override suspend fun get(id: DeviceTypeId): Either<AbstractBackendException, DeviceTypeModel> =
        DeviceTypes.select(id) { fold(ifEmpty = { deviceTypeByIdNotFound(id) }, mapper::invoke) }

    override suspend fun post(createModel: DeviceTypeCreateModel): Either<AbstractBackendException, DeviceTypeModel> {
        val deviceTypeId = DeviceTypeId(UUID.randomUUID())
        DeviceTypes.insert(deviceTypeId)
        return DeviceTypes.select(deviceTypeId) { fold(ifEmpty = { failedToCreateDeviceType() }, mapper::invoke) }
    }

    override suspend fun post(
        id: DeviceTypeId,
        createModel: DeviceTypeCreateModel
    ): Either<AbstractBackendException, DeviceTypeModel> =
        DeviceTypes.select(id) { fold(ifEmpty = { failedToCreateDeviceType() }, mapper::invoke) }

    override suspend fun put(
        id: DeviceTypeId,
        updateModel: DeviceTypeUpdateModel
    ): Either<AbstractBackendException, DeviceTypeModel> =
        Either.backend {
            DeviceTypes.update(
                id = id,
            )
        }.flatMap {
            get(id)
        }

    override suspend fun delete(id: DeviceTypeId): Either<AbstractBackendException, Boolean> =
        (DeviceTypes.delete(id) > 0).right()
}