package ru.novolmob.jdbcbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import ru.novolmob.backendapi.exceptions.*
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceDetailCreateModel
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.backendapi.models.DeviceDetailUpdateModel
import ru.novolmob.backendapi.repositories.IDeviceDetailRepository
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateTableFunction
import ru.novolmob.jdbcdatabase.tables.DeviceDetails
import java.sql.ResultSet

class DeviceDetailRepositoryImpl(
    mapper: Mapper<ResultSet, DeviceDetailModel>
): IDeviceDetailRepository, AbstractDetailRepository<DeviceDetailId, DeviceId, DeviceDetailModel, DeviceDetailCreateModel, DeviceDetailUpdateModel>(
    DeviceDetails, mapper, ::deviceDetailByIdNotFound, ::deviceDetailByDeviceIdAndLanguageNotFound
) {
    override suspend fun post(createModel: DeviceDetailCreateModel): Either<AbstractBackendException, DeviceDetailModel> =
        getDetailFor(createModel.parentId, createModel.language).fold(
            ifLeft = {
                CreationOrUpdateTableFunction.CreationOrUpdateDeviceDetailFunction.call(
                    deviceId = createModel.parentId,
                    title = createModel.title,
                    description = createModel.description,
                    features = createModel.features,
                    language = createModel.language
                ) {
                    fold(ifEmpty = { failedToCreateDeviceDetail() }, mapper::invoke)
                }
            },
            ifRight = {
                post(it.id, createModel)
            }
        )

    override suspend fun post(
        id: DeviceDetailId,
        createModel: DeviceDetailCreateModel
    ): Either<AbstractBackendException, DeviceDetailModel> {
        if (!DeviceDetails.check(id, createModel.parentId, createModel.language))
            return detailWithParentIDAndLanguageIsExists(createModel.parentId, createModel.language).left()
        return CreationOrUpdateTableFunction.CreationOrUpdateDeviceDetailFunction.call(
            id = id,
            deviceId = createModel.parentId,
            title = createModel.title,
            description = createModel.description,
            features = createModel.features,
            language = createModel.language
        ) {
            fold(ifEmpty = { deviceDetailByIdNotFound(id) }, mapper::invoke)
        }
    }

    override suspend fun put(
        id: DeviceDetailId,
        updateModel: DeviceDetailUpdateModel
    ): Either<AbstractBackendException, DeviceDetailModel> {
        return get(id).fold(
            ifLeft = { deviceDetailByIdNotFound(id).left() },
            ifRight = {
                val parentId = updateModel.parentId ?: it.parentId
                val language = updateModel.language ?: it.language
                if (!DeviceDetails.check(id, parentId, language))
                    return detailWithParentIDAndLanguageIsExists(parentId, language).left()
                DeviceDetails.update(
                    id = id,
                    deviceId = updateModel.parentId,
                    title = updateModel.title,
                    description = updateModel.description,
                    features = updateModel.features,
                    language = updateModel.language
                )
                get(id)
            }
        )
    }
}