package ru.novolmob.jdbcbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import ru.novolmob.backendapi.exceptions.*
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderStatusDetailCreateModel
import ru.novolmob.backendapi.models.OrderStatusDetailModel
import ru.novolmob.backendapi.models.OrderStatusDetailUpdateModel
import ru.novolmob.backendapi.repositories.IOrderStatusDetailRepository
import ru.novolmob.core.models.ids.OrderStatusDetailId
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateTableFunction
import ru.novolmob.jdbcdatabase.tables.OrderStatusDetails
import java.sql.ResultSet

class OrderStatusDetailDetailRepositoryImpl(
    mapper: Mapper<ResultSet, OrderStatusDetailModel>
): IOrderStatusDetailRepository, AbstractDetailRepository<OrderStatusDetailId, OrderStatusId, OrderStatusDetailModel, OrderStatusDetailCreateModel, OrderStatusDetailUpdateModel>(
    OrderStatusDetails, mapper, ::orderStatusDetailByIdNotFound, ::orderStatusDetailByDeviceIdAndLanguageNotFound
) {
    override suspend fun post(createModel: OrderStatusDetailCreateModel): Either<AbstractBackendException, OrderStatusDetailModel> =
        getDetailFor(createModel.parentId, createModel.language).fold(
            ifLeft = {
                CreationOrUpdateTableFunction.CreateOrUpdateOrderStatusDetailFunction.call(
                    orderStatusId = createModel.parentId,
                    title = createModel.title,
                    description = createModel.description,
                    language = createModel.language
                ) {
                    fold(ifEmpty = { failedToCreateOrderStatusDetail() }, mapper::invoke)
                }
            },
            ifRight = {
                post(it.id, createModel)
            }
        )

    override suspend fun post(
        id: OrderStatusDetailId,
        createModel: OrderStatusDetailCreateModel
    ): Either<AbstractBackendException, OrderStatusDetailModel> {
        if (!OrderStatusDetails.check(id, createModel.parentId, createModel.language))
            return detailWithParentIDAndLanguageIsExists(createModel.parentId, createModel.language).left()
        return CreationOrUpdateTableFunction.CreateOrUpdateOrderStatusDetailFunction.call(
            id = id,
            orderStatusId = createModel.parentId,
            title = createModel.title,
            description = createModel.description,
            language = createModel.language
        ) {
            fold(ifEmpty = { orderStatusDetailByIdNotFound(id) }, mapper::invoke)
        }
    }

    override suspend fun put(
        id: OrderStatusDetailId,
        updateModel: OrderStatusDetailUpdateModel
    ): Either<AbstractBackendException, OrderStatusDetailModel> {
        return get(id).fold(
            ifLeft = { orderStatusDetailByIdNotFound(id).left() },
            ifRight = {
                val parentId = updateModel.parentId ?: it.parentId
                val language = updateModel.language ?: it.language
                if (!OrderStatusDetails.check(id, parentId, language))
                    return detailWithParentIDAndLanguageIsExists(parentId, language).left()
                OrderStatusDetails.update(
                    id = id,
                    orderStatusId = updateModel.parentId,
                    title = updateModel.title,
                    description = updateModel.description,
                    language = updateModel.language
                )
                get(id)
            }
        )
    }
}