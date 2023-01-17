package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId

interface ICityRepository: ICrudRepository<CityId, CityModel, CityCreateModel, CityUpdateModel> {
    suspend fun getFull(cityId: CityId, language: Language): Either<AbstractBackendException, CityFullModel>
    suspend fun getShort(cityId: CityId, language: Language): Either<AbstractBackendException, CityShortModel>
    suspend fun getAll(pagination: Pagination, language: Language): Either<AbstractBackendException, Page<CityShortModel>>
}