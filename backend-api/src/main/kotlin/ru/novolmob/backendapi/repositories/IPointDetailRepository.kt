package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.PointDetailCreateModel
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.backendapi.models.PointDetailUpdateModel
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId

interface IPointDetailRepository: ICrudRepository<PointDetailId, PointDetailModel, PointDetailCreateModel, PointDetailUpdateModel> {
    suspend fun getDetailFor(pointId: PointId, language: Language): Either<AbstractBackendException, PointDetailModel>
    suspend fun removeFor(pointId: PointId): Either<AbstractBackendException, Boolean>
}