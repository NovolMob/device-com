package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.database.models.Amount
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.PointId
import ru.novolmob.database.models.ids.PointToDeviceEntityId

interface IPointToDeviceRepository: ICrudRepository<PointToDeviceEntityId, PointToDeviceEntityModel, PointToDeviceEntityCreateModel, PointToDeviceEntityUpdateModel> {
    suspend fun getDevices(pointId: PointId): Either<BackendException, List<PointItemModel>>
    suspend fun getPoints(deviceId: DeviceId, language: Language): Either<BackendException, List<NumberOfDeviceInPointModel>>
    suspend fun setInPoint(pointId: PointId, deviceId: DeviceId, amount: Amount): Either<BackendException, Boolean>
    suspend fun removeFromPoint(pointId: PointId, deviceId: DeviceId): Either<BackendException, Boolean>
    suspend fun getNumberOfDevicesInPoint(pointId: PointId, deviceId: DeviceId): Either<BackendException, Amount>
}