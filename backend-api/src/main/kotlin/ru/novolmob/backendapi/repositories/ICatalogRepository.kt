package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.CatalogModel
import ru.novolmob.backendapi.models.CatalogSearchSample
import ru.novolmob.core.models.Language

interface ICatalogRepository: IRepository {
    suspend fun getCatalog(sample: CatalogSearchSample, language: Language): Either<BackendException, CatalogModel>
}