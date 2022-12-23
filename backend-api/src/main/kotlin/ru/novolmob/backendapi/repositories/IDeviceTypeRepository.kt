package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceTypeId

interface IDeviceTypeRepository: ICrudRepository<DeviceTypeId, DeviceTypeModel, DeviceTypeCreateModel, DeviceTypeUpdateModel> {
    suspend fun getFull(deviceTypeId: DeviceTypeId, language: Language): Either<BackendException, DeviceTypeFullModel>
    suspend fun getAll(pagination: Pagination, language: Language): Either<BackendException, Page<DeviceTypeShortModel>>
}