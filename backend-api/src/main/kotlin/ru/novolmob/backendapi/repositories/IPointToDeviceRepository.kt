package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.PointItemModel
import ru.novolmob.backendapi.models.PointToDeviceEntityCreateModel
import ru.novolmob.backendapi.models.PointToDeviceEntityModel
import ru.novolmob.backendapi.models.PointToDeviceEntityUpdateModel
import ru.novolmob.database.models.ids.PointId
import ru.novolmob.database.models.ids.PointToDeviceEntityId

interface IPointToDeviceRepository: ICrudRepository<PointToDeviceEntityId, PointToDeviceEntityModel, PointToDeviceEntityCreateModel, PointToDeviceEntityUpdateModel> {
    suspend fun getDevices(pointId: PointId): Either<BackendException, List<PointItemModel>>
}