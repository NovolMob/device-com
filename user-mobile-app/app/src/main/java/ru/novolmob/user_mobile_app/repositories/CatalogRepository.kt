package ru.novolmob.user_mobile_app.repositories

import arrow.core.Either
import io.ktor.client.*
import ru.novolmob.backend.ktorrouting.user.Catalog
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.CatalogModel
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.user_mobile_app.utils.KtorUtil.get

interface ICatalogRepository: IRepository {
    suspend fun getCatalog(sample: Catalog): Either<BackendException, CatalogModel>
}

class CatalogRepositoryImpl(
    private val client: HttpClient
): ICatalogRepository {
    override suspend fun getCatalog(sample: Catalog): Either<BackendException, CatalogModel> =
        client.get(resource = sample)
}