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
import ru.novolmob.backendapi.exceptions.cityByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.ICityDetailRepository
import ru.novolmob.backendapi.repositories.ICityRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.exposedbackendapi.utils.RepositoryUtil
import ru.novolmob.exposeddatabase.entities.City
import ru.novolmob.exposeddatabase.tables.Cities

class CityRepositoryImpl(
    mapper: Mapper<City, CityModel>,
    resultRowMapper: Mapper<ResultRow, CityModel>,
    val cityDetailRepository: ICityDetailRepository
): ICityRepository, AbstractCrudRepository<CityId, City.Companion, City, CityModel, CityCreateModel, CityUpdateModel>(
    City.Companion, mapper, resultRowMapper, ::cityByIdNotFound
) {
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

    override fun City.Companion.new(createModel: CityCreateModel): Either<AbstractBackendException, City> {
        return new {  }.right()
    }

    override fun City.applyC(createModel: CityCreateModel): Either<AbstractBackendException, City> {
        return apply {

        }.right()
    }

    override fun City.applyU(updateModel: CityUpdateModel): Either<AbstractBackendException, City> {
        return apply {

        }.right()
    }

}