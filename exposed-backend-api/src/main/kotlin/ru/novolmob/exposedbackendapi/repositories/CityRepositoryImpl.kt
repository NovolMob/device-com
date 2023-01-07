package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.ICityDetailRepository
import ru.novolmob.backendapi.repositories.ICityRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.exposedbackendapi.exceptions.cityByIdNotFound
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.exposeddatabase.entities.City
import ru.novolmob.exposeddatabase.tables.Cities

class CityRepositoryImpl(
    val mapper: Mapper<City, CityModel>,
    val resultRowMapper: Mapper<ResultRow, CityModel>,
    val cityDetailRepository: ICityDetailRepository
): ICityRepository {
    override suspend fun getFull(cityId: CityId, language: Language): Either<AbstractBackendException, CityFullModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            City.findById(cityId)?.let { city ->
                cityDetailRepository.getDetailFor(city.id.value, language).flatMap { detail ->
                    CityFullModel(
                        id = city.id.value,
                        detail = detail
                    ).right()
                }
            } ?: cityByIdNotFound(cityId).left()
        }

    override suspend fun getShort(
        cityId: CityId,
        language: Language
    ): Either<AbstractBackendException, CityShortModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            City.findById(cityId)?.let { city ->
                cityDetailRepository.getDetailFor(city.id.value, language).flatMap { detail ->
                    CityShortModel(
                        id = city.id.value,
                        title = detail.title
                    ).right()
                }
            } ?: cityByIdNotFound(cityId).left()
        }

    override suspend fun getAll(pagination: Pagination, language: Language): Either<AbstractBackendException, Page<CityShortModel>> =
        RepositoryUtil.generalGetAll(Cities, pagination, resultRowMapper).flatMap { page ->
            page.list
                .parTraverseEither { city ->
                    cityDetailRepository.getDetailFor(city.id, language).flatMap { detail ->
                        CityShortModel(
                            id = city.id,
                            title = detail.title
                        ).right()
                    }
                }.flatMap {
                    Page(
                        page = page.page,
                        size = it.size.toLong(),
                        list = it
                    ).right()
                }
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<CityModel>> =
        RepositoryUtil.generalGetAll(Cities, pagination, resultRowMapper)

    override suspend fun get(id: CityId): Either<AbstractBackendException, CityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            City.findById(id)?.let(mapper::invoke) ?: cityByIdNotFound(id).left()
        }

    override suspend fun post(createModel: CityCreateModel): Either<AbstractBackendException, CityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            City.new { }.let(mapper::invoke)
        }

    override suspend fun post(id: CityId, createModel: CityCreateModel): Either<AbstractBackendException, CityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            City.findById(id)?.apply {

            }?.let(mapper::invoke) ?: cityByIdNotFound(id).left()
        }

    override suspend fun put(id: CityId, updateModel: CityUpdateModel): Either<AbstractBackendException, CityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            City.findById(id)?.apply {

            }?.let(mapper::invoke) ?: cityByIdNotFound(id).left()
        }

    override suspend fun delete(id: CityId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            City.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

}