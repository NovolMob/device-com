package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.CityDetailCreateModel
import ru.novolmob.backendapi.models.CityDetailModel
import ru.novolmob.backendapi.models.CityDetailUpdateModel
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId

interface ICityDetailRepository: ICrudRepository<CityDetailId, CityDetailModel, CityDetailCreateModel, CityDetailUpdateModel> {
    suspend fun getDetailFor(cityId: CityId, language: Language): Either<AbstractBackendException, CityDetailModel>
    suspend fun removeFor(cityId: CityId): Either<AbstractBackendException, Boolean>
}