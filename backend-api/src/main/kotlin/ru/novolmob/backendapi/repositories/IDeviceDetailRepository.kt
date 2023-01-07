package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.DeviceDetailCreateModel
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.backendapi.models.DeviceDetailUpdateModel
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId

interface IDeviceDetailRepository: ICrudRepository<DeviceDetailId, DeviceDetailModel, DeviceDetailCreateModel, DeviceDetailUpdateModel> {
    suspend fun getDetailFor(deviceId: DeviceId, language: Language): Either<AbstractBackendException, DeviceDetailModel>
    suspend fun removeDetailFor(deviceId: DeviceId): Either<AbstractBackendException, Boolean>
}