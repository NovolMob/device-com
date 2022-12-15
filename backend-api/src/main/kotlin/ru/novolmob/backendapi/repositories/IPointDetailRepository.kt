package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.PointDetailCreateModel
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.backendapi.models.PointDetailUpdateModel
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.PointDetailId
import ru.novolmob.database.models.ids.PointId

interface IPointDetailRepository: ICrudRepository<PointDetailId, PointDetailModel, PointDetailCreateModel, PointDetailUpdateModel> {
    suspend fun getDetailFor(pointId: PointId, language: Language): Either<BackendException, PointDetailModel>
    suspend fun removeFor(pointId: PointId): Either<BackendException, Boolean>
}