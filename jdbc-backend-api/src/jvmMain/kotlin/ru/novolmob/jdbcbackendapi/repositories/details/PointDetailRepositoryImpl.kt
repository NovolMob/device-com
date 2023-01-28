package ru.novolmob.jdbcbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import ru.novolmob.backendapi.exceptions.*
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.PointDetailCreateModel
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.backendapi.models.PointDetailUpdateModel
import ru.novolmob.backendapi.repositories.IPointDetailRepository
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateTableFunction
import ru.novolmob.jdbcdatabase.tables.PointDetails
import java.sql.ResultSet

class PointDetailRepositoryImpl(
    mapper: Mapper<ResultSet, PointDetailModel>
): IPointDetailRepository, AbstractDetailRepository<PointDetailId, PointId, PointDetailModel, PointDetailCreateModel, PointDetailUpdateModel>(
    PointDetails, mapper, ::pointDetailByIdNotFound, ::pointDetailByPointIdAndLanguageNotFound
) {
    override suspend fun post(createModel: PointDetailCreateModel): Either<AbstractBackendException, PointDetailModel> =
        getDetailFor(createModel.parentId, createModel.language).fold(
            ifLeft = {
                CreationOrUpdateTableFunction.CreationOrUpdatePointDetailFunction.call(
                    pointId = createModel.parentId,
                    address = createModel.address,
                    schedule = createModel.schedule,
                    description = createModel.description,
                    language = createModel.language
                ) {
                    fold(ifEmpty = { failedToCreatePointDetail() }, mapper::invoke)
                }
            },
            ifRight = {
                post(it.id, createModel)
            }
        )

    override suspend fun post(
        id: PointDetailId,
        createModel: PointDetailCreateModel
    ): Either<AbstractBackendException, PointDetailModel> {
        if (!PointDetails.check(id, createModel.parentId, createModel.language))
            return detailWithParentIDAndLanguageIsExists(createModel.parentId, createModel.language).left()
        return CreationOrUpdateTableFunction.CreationOrUpdatePointDetailFunction.call(
            id = id,
            pointId = createModel.parentId,
            address = createModel.address,
            schedule = createModel.schedule,
            description = createModel.description,
            language = createModel.language
        ) {
            fold(ifEmpty = { pointDetailByIdNotFound(id) }, mapper::invoke)
        }
    }

    override suspend fun put(
        id: PointDetailId,
        updateModel: PointDetailUpdateModel
    ): Either<AbstractBackendException, PointDetailModel> {
        return get(id).fold(
            ifLeft = { pointDetailByIdNotFound(id).left() },
            ifRight = {
                val parentId = updateModel.parentId ?: it.parentId
                val language = updateModel.language ?: it.language
                if (!PointDetails.check(id, parentId, language))
                    return detailWithParentIDAndLanguageIsExists(parentId, language).left()
                PointDetails.update(
                    id = id,
                    pointId = updateModel.parentId,
                    address = updateModel.address,
                    schedule = updateModel.schedule,
                    description = updateModel.description,
                    language = updateModel.language
                )
                get(id)
            }
        )
    }
}