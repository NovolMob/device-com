package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.computations.either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.CatalogModel
import ru.novolmob.backendapi.models.CatalogSearchSample
import ru.novolmob.backendapi.models.DeviceShortModel
import ru.novolmob.backendapi.repositories.ICatalogRepository
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.core.models.Language
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.list
import ru.novolmob.jdbcdatabase.functions.GettingCatalogFunction
import ru.novolmob.jdbcdatabase.procedures.GetAmountOfPagesProcedure
import java.sql.ResultSet

class CatalogRepositoryImpl(
    val mapper: Mapper<ResultSet, DeviceShortModel>
): ICatalogRepository {
    override suspend fun getCatalog(
        sample: CatalogSearchSample,
        language: Language
    ): Either<AbstractBackendException, CatalogModel> =
        either {
            withContext(Dispatchers.Default) {
                val page = sample.page ?: 0
                val pageSize = sample.pageSize ?: 1
                val searchString = sample.searchString ?: ""
                val amountOfPages = async {
                    Either.backend {
                        GetAmountOfPagesProcedure.call(
                            pageSize = pageSize,
                            searchString = searchString,
                            deviceTypeId = sample.deviceTypeId,
                            language = language
                        )
                    }
                }
                val devices = async {
                    GettingCatalogFunction.call(
                        page = page, pageSize = pageSize,
                        searchString = searchString,
                        deviceTypeId = sample.deviceTypeId,
                        language = language
                    ) { list(mapper) }
                }
                CatalogModel(
                    page = page, pageSize = pageSize,
                    devices = devices.await().bind(),
                    amountOfPages = amountOfPages.await().bind()
                )
            }
        }
}