package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.deviceByIdNotFound
import ru.novolmob.backendapi.exceptions.failedToCreateDevice
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceCreateModel
import ru.novolmob.backendapi.models.DeviceFullModel
import ru.novolmob.backendapi.models.DeviceModel
import ru.novolmob.backendapi.models.DeviceUpdateModel
import ru.novolmob.backendapi.repositories.IDeviceRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateTableFunction
import ru.novolmob.jdbcdatabase.tables.Devices
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

class DeviceRepositoryImpl(
    mapper: Mapper<ResultSet, DeviceModel>,
    val fullModelMapper: Mapper<ResultSet, DeviceFullModel>
): IDeviceRepository, AbstractCrudTableRepository<DeviceId, DeviceModel, DeviceCreateModel, DeviceUpdateModel>(
    Devices, mapper, ::deviceByIdNotFound
) {
    override suspend fun getFull(
        id: DeviceId,
        language: Language
    ): Either<AbstractBackendException, DeviceFullModel> =
        DetailView.DeviceDetailView.select(id, language) { fold(ifEmpty = { deviceByIdNotFound(id) }, fullModelMapper::invoke) }

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
}