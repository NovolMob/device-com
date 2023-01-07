package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.core.models.ids.PointId

interface IPointRepository: ICrudRepository<PointId, PointModel, PointCreateModel, PointUpdateModel> {
    suspend fun getFull(pointId: PointId, language: Language): Either<AbstractBackendException, PointFullModel>
    suspend fun getShort(pointId: PointId, language: Language): Either<AbstractBackendException, PointShortModel>
    suspend fun getByCity(cityId: CityId, language: Language): Either<AbstractBackendException, List<PointShortModel>>
    suspend fun getAll(pagination: Pagination, language: Language): Either<AbstractBackendException, Page<PointShortModel>>
}