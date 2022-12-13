package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.DeviceTypeDetailCreateModel
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.backendapi.models.DeviceTypeDetailUpdateModel
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.DeviceTypeDetailId
import ru.novolmob.database.models.ids.DeviceTypeId

interface IDeviceTypeDetailRepository: ICrudRepository<DeviceTypeDetailId, DeviceTypeDetailModel, DeviceTypeDetailCreateModel, DeviceTypeDetailUpdateModel> {
    suspend fun getDetailFor(deviceTypeId: DeviceTypeId, language: Language): Either<BackendException, DeviceTypeDetailModel>
    suspend fun removeDetailFor(deviceTypeId: DeviceTypeId): Either<BackendException, Boolean>
}