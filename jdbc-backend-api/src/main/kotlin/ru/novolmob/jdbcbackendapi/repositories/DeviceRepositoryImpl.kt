package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.deviceByIdNotFound
import ru.novolmob.backendapi.exceptions.failedToCreateDevice
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IDeviceRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.jdbcbackendapi.utils.RepositoryUtil
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateTableFunction
import ru.novolmob.jdbcdatabase.tables.Devices
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class DeviceRepositoryImpl(
    val mapper: Mapper<ResultSet, DeviceModel>,
    val fullModelMapper: Mapper<ResultSet, DeviceFullModel>
): IDeviceRepository {
    override suspend fun getFull(
        id: DeviceId,
        language: Language
    ): Either<AbstractBackendException, DeviceFullModel> =
        DetailView.DeviceDetailView.select(id, language) { fold(ifEmpty = { deviceByIdNotFound(id) }, fullModelMapper::invoke) }

    override suspend fun get(id: DeviceId): Either<AbstractBackendException, DeviceModel> =
        Devices.select(id) { fold(ifEmpty = { deviceByIdNotFound(id) }, mapper::invoke) }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<DeviceModel>> =
        RepositoryUtil.getAll(Devices, pagination, mapper)

    override suspend fun post(createModel: DeviceCreateModel): Either<AbstractBackendException, DeviceModel> =
        CreationOrUpdateTableFunction.CreationOrUpdateDeviceFunction.call(
            code = createModel.article, typeId = createModel.typeId,
            amount = createModel.amount, price = createModel.price
        ) { fold(ifEmpty = { failedToCreateDevice() }, mapper::invoke) }

    override suspend fun post(
        id: DeviceId,
        createModel: DeviceCreateModel
    ): Either<AbstractBackendException, DeviceModel> =
        CreationOrUpdateTableFunction.CreationOrUpdateDeviceFunction.call(
            id = id,
            code = createModel.article, typeId = createModel.typeId,
            amount = createModel.amount, price = createModel.price
        ) { fold(ifEmpty = { deviceByIdNotFound(id) }, mapper::invoke) }

    override suspend fun put(
        id: DeviceId,
        updateModel: DeviceUpdateModel
    ): Either<AbstractBackendException, DeviceModel> {
        if (listOf(updateModel.typeId, updateModel.article, updateModel.amount, updateModel.price).any { it != null }) {
            Devices.update(
                id = id, code = updateModel.article, typeId = updateModel.typeId,
                amount = updateModel.amount, price = updateModel.price
            )
        }
        return Devices.select(id) { fold(ifEmpty = { deviceByIdNotFound(id) }, mapper::invoke) }
    }

    override suspend fun delete(id: DeviceId): Either<AbstractBackendException, Boolean> =
        (Devices.delete(id) > 0).right()
}