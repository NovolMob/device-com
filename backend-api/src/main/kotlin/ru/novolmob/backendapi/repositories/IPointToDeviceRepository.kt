package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.PointId
import ru.novolmob.database.models.ids.PointToDeviceEntityId

interface IPointToDeviceRepository: ICrudRepository<PointToDeviceEntityId, PointToDeviceEntityModel, PointToDeviceEntityCreateModel, PointToDeviceEntityUpdateModel> {
    suspend fun getDevices(pointId: PointId): Either<BackendException, List<PointItemModel>>
    suspend fun getPoints(deviceId: DeviceId): Either<BackendException, List<NumberOfDeviceInPointModel>>
}