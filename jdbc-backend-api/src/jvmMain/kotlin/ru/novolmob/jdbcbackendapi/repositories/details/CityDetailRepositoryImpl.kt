package ru.novolmob.jdbcbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import ru.novolmob.backendapi.exceptions.*
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.CityDetailCreateModel
import ru.novolmob.backendapi.models.CityDetailModel
import ru.novolmob.backendapi.models.CityDetailUpdateModel
import ru.novolmob.backendapi.repositories.ICityDetailRepository
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateTableFunction
import ru.novolmob.jdbcdatabase.tables.CityDetails
import java.sql.ResultSet

class CityDetailRepositoryImpl(
    mapper: Mapper<ResultSet, CityDetailModel>
): ICityDetailRepository, AbstractDetailRepository<CityDetailId, CityId, CityDetailModel, CityDetailCreateModel, CityDetailUpdateModel>(
    CityDetails, mapper, ::cityDetailByIdNotFound, ::cityDetailByCityIdAndLanguageNotFound
) {
    override suspend fun post(createModel: CityDetailCreateModel): Either<AbstractBackendException, CityDetailModel> =
        getDetailFor(createModel.parentId, createModel.language).fold(
            ifLeft = {
                CreationOrUpdateTableFunction.CreationOrUpdateCityDetailFunction.call(
                    cityId = createModel.parentId,
                    title = createModel.title,
                    language = createModel.language
                ) {
                    fold(ifEmpty = { failedToCreateCityDetail() }, mapper::invoke)
                }
            },
            ifRight = {
                post(it.id, createModel)
            }
        )

    override suspend fun post(
        id: CityDetailId,
        createModel: CityDetailCreateModel
    ): Either<AbstractBackendException, CityDetailModel> {
        if (!CityDetails.check(id, createModel.parentId, createModel.language))
            return detailWithParentIDAndLanguageIsExists(createModel.parentId, createModel.language).left()
        return CreationOrUpdateTableFunction.CreationOrUpdateCityDetailFunction.call(
            id = id,
            cityId = createModel.parentId,
            title = createModel.title,
            language = createModel.language
        ) {
            fold(ifEmpty = { cityDetailByIdNotFound(id) }, mapper::invoke)
        }
    }

    override suspend fun put(
        id: CityDetailId,
        updateModel: CityDetailUpdateModel
    ): Either<AbstractBackendException, CityDetailModel> {
        return get(id).fold(
            ifLeft = { cityDetailByIdNotFound(id).left() },
            ifRight = {
                val parentId = updateModel.parentId ?: it.parentId
                val language = updateModel.language ?: it.language
                if (!CityDetails.check(id, parentId, language))
                    return detailWithParentIDAndLanguageIsExists(parentId, language).left()
                CityDetails.update(
                    id = id,
                    cityId = updateModel.parentId,
                    title = updateModel.title,
                    language = updateModel.language
                )
                get(id)
            }
        )
    }
}