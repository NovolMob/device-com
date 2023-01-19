package ru.novolmob.jdbcbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import ru.novolmob.backendapi.exceptions.*
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DeviceTypeDetailCreateModel
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.backendapi.models.DeviceTypeDetailUpdateModel
import ru.novolmob.backendapi.repositories.IDeviceTypeDetailRepository
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateTableFunction
import ru.novolmob.jdbcdatabase.tables.DeviceTypeDetails
import java.sql.ResultSet

class DeviceTypeDetailRepositoryImpl(
    mapper: Mapper<ResultSet, DeviceTypeDetailModel>
): IDeviceTypeDetailRepository, AbstractDetailRepository<DeviceTypeDetailId, DeviceTypeId, DeviceTypeDetailModel, DeviceTypeDetailCreateModel, DeviceTypeDetailUpdateModel>(
    DeviceTypeDetails, mapper, ::deviceTypeDetailByIdNotFound, ::deviceTypeDetailByDeviceIdAndLanguageNotFound
) {
    override suspend fun post(createModel: DeviceTypeDetailCreateModel): Either<AbstractBackendException, DeviceTypeDetailModel> =
        getDetailFor(createModel.parentId, createModel.language).fold(
            ifLeft = {
                CreationOrUpdateTableFunction.CreateOrUpdateDeviceTypeDetailFunction.call(
                    deviceTypeId = createModel.parentId,
                    title = createModel.title,
                    description = createModel.description,
                    language = createModel.language
                ) {
                    fold(ifEmpty = { failedToCreateDeviceTypeDetail() }, mapper::invoke)
                }
            },
            ifRight = {
                post(it.id, createModel)
            }
        )

    override suspend fun post(
        id: DeviceTypeDetailId,
        createModel: DeviceTypeDetailCreateModel
    ): Either<AbstractBackendException, DeviceTypeDetailModel> {
        if (!DeviceTypeDetails.check(id, createModel.parentId, createModel.language))
            return detailWithParentIDAndLanguageIsExists(createModel.parentId, createModel.language).left()
        return CreationOrUpdateTableFunction.CreateOrUpdateDeviceTypeDetailFunction.call(
            id = id,
            deviceTypeId = createModel.parentId,
            title = createModel.title,
            description = createModel.description,
            language = createModel.language
        ) {
            fold(ifEmpty = { deviceTypeDetailByIdNotFound(id) }, mapper::invoke)
        }
    }

    override suspend fun put(
        id: DeviceTypeDetailId,
        updateModel: DeviceTypeDetailUpdateModel
    ): Either<AbstractBackendException, DeviceTypeDetailModel> {
        return get(id).fold(
            ifLeft = { deviceTypeDetailByIdNotFound(id).left() },
            ifRight = {
                val parentId = updateModel.parentId ?: it.parentId
                val language = updateModel.language ?: it.language
                if (!DeviceTypeDetails.check(id, parentId, language))
                    return detailWithParentIDAndLanguageIsExists(parentId, language).left()
                DeviceTypeDetails.update(
                    id = id,
                    deviceTypeId = updateModel.parentId,
                    title = updateModel.title,
                    description = updateModel.description,
                    language = updateModel.language
                )
                get(id)
            }
        )
    }
}