package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.PointId

interface IPointRepository: ICrudRepository<PointId, PointModel, PointCreateModel, PointUpdateModel> {
    suspend fun getFull(pointId: PointId, language: Language): Either<BackendException, PointFullModel>
}