package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.DeviceTypeCreateModel
import ru.novolmob.backendapi.models.DeviceTypeFullModel
import ru.novolmob.backendapi.models.DeviceTypeModel
import ru.novolmob.backendapi.models.DeviceTypeUpdateModel
import ru.novolmob.database.models.ids.DeviceTypeId

interface IDeviceTypeRepository: ICrudRepository<DeviceTypeId, DeviceTypeModel, DeviceTypeCreateModel, DeviceTypeUpdateModel> {
    suspend fun getFull(deviceTypeId: DeviceTypeId): Either<BackendException, DeviceTypeFullModel>
}