package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.DeviceTypeDetailCreateModel
import ru.novolmob.backendapi.models.DeviceTypeDetailModel
import ru.novolmob.backendapi.models.DeviceTypeDetailUpdateModel
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId

interface IDeviceTypeDetailRepository: ICrudRepository<DeviceTypeDetailId, DeviceTypeDetailModel, DeviceTypeDetailCreateModel, DeviceTypeDetailUpdateModel> {
    suspend fun getDetailFor(deviceTypeId: DeviceTypeId, language: Language): Either<AbstractBackendException, DeviceTypeDetailModel>
    suspend fun removeDetailFor(deviceTypeId: DeviceTypeId): Either<AbstractBackendException, Boolean>
}