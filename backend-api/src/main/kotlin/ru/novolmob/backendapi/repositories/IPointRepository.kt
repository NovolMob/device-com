package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.City
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.PointId

interface IPointRepository: ICrudRepository<PointId, PointModel, PointCreateModel, PointUpdateModel> {
    suspend fun getFull(pointId: PointId, language: Language): Either<BackendException, PointFullModel>
    suspend fun getShort(pointId: PointId, language: Language): Either<BackendException, PointShortModel>
    suspend fun getByCity(city: City, language: Language): Either<BackendException, List<PointShortModel>>
    suspend fun getAll(language: Language): Either<BackendException, List<PointShortModel>>
}