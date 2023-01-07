package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.DeviceCreateModel
import ru.novolmob.backendapi.models.DeviceFullModel
import ru.novolmob.backendapi.models.DeviceModel
import ru.novolmob.backendapi.models.DeviceUpdateModel
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceId

interface IDeviceRepository: ICrudRepository<DeviceId, DeviceModel, DeviceCreateModel, DeviceUpdateModel> {
    suspend fun getFull(deviceId: DeviceId, language: Language): Either<AbstractBackendException, DeviceFullModel>
}