package ru.novolmob.user_mobile_app.repositories

import arrow.core.Either
import io.ktor.client.*
import ru.novolmob.backend.ktorrouting.user.Cities
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.CityFullModel
import ru.novolmob.backendapi.models.CityShortModel
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.user_mobile_app.utils.KtorUtil.get

interface ICityRepository: IRepository {
    suspend fun getAll(language: Language): Either<AbstractBackendException, Page<CityShortModel>>
    suspend fun get(cityId: CityId): Either<AbstractBackendException, CityFullModel>
}

class CityRepositoryImpl(
    val client: HttpClient
): ICityRepository {
    override suspend fun getAll(language: Language): Either<AbstractBackendException, Page<CityShortModel>> =
        client.get(Cities(language = language))

    override suspend fun get(cityId: CityId): Either<AbstractBackendException, CityFullModel> =
        client.get(Cities.Id(cities = Cities(language = Language("")), id = cityId))

}