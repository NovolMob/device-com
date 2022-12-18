package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.PointToDeviceEntityId

interface IPointToDeviceRepository: ICrudRepository<PointToDeviceEntityId, PointToDeviceEntityModel, PointToDeviceEntityCreateModel, PointToDeviceEntityUpdateModel> {
    suspend fun getDevices(pointId: PointId): Either<BackendException, List<PointItemModel>>
    suspend fun getPoints(deviceId: DeviceId, language: Language): Either<BackendException, List<NumberOfDeviceInPointModel>>
    suspend fun setInPoint(pointId: PointId, deviceId: DeviceId, amount: Amount): Either<BackendException, Boolean>
    suspend fun removeFromPoint(pointId: PointId, deviceId: DeviceId): Either<BackendException, Boolean>
    suspend fun getNumberOfDevicesInPoint(pointId: PointId, deviceId: DeviceId): Either<BackendException, Amount>
}