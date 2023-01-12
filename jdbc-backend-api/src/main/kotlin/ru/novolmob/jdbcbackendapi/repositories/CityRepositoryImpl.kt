package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.cityByIdNotFound
import ru.novolmob.backendapi.exceptions.failedToCreateCity
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.ICityRepository
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.jdbcbackendapi.utils.RepositoryUtil
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.tables.Cities
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet
import java.util.UUID

class CityRepositoryImpl(
    val mapper: Mapper<ResultSet, CityModel>,
    val shortModelMapper: Mapper<ResultSet, CityShortModel>,
    val fullModelMapper: Mapper<ResultSet, CityFullModel>,
): ICityRepository {
    override suspend fun getFull(cityId: CityId, language: Language): Either<AbstractBackendException, CityFullModel> =
        DetailView.CityDetailView.select(cityId, language) { fold(ifEmpty = { cityByIdNotFound(cityId) }, fullModelMapper::invoke) }

    override suspend fun getShort(
        cityId: CityId,
        language: Language
    ): Either<AbstractBackendException, CityShortModel> =
        DetailView.CityDetailView.select(cityId, language) { fold(ifEmpty = { cityByIdNotFound(cityId) }, shortModelMapper::invoke) }

    override suspend fun getAll(
        pagination: Pagination,
        language: Language
    ): Either<AbstractBackendException, Page<CityShortModel>> =
        RepositoryUtil.getAll(DetailView.CityDetailView, pagination, language, shortModelMapper)

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<CityModel>> =
        RepositoryUtil.getAll(Cities, pagination, mapper)

    override suspend fun get(id: CityId): Either<AbstractBackendException, CityModel> =
        Cities.select(id) { fold(ifEmpty = { cityByIdNotFound(id) }, mapper::invoke) }

    override suspend fun post(createModel: CityCreateModel): Either<AbstractBackendException, CityModel> {
        val cityId = CityId(UUID.randomUUID())
        Cities.insert(cityId)
        return Cities.select(cityId) { fold(ifEmpty = { failedToCreateCity() }, mapper::invoke) }
    }

    override suspend fun post(id: CityId, createModel: CityCreateModel): Either<AbstractBackendException, CityModel> =
        Cities.select(id) { fold(ifEmpty = { cityByIdNotFound(id) }, mapper::invoke) }

    override suspend fun put(id: CityId, updateModel: CityUpdateModel): Either<AbstractBackendException, CityModel> =
        Either.backend {
            Cities.update(
                id = id,
            )
        }.flatMap {
            get(id)
        }

    override suspend fun delete(id: CityId): Either<AbstractBackendException, Boolean> =
        (Cities.delete(id) > 0).right()
}