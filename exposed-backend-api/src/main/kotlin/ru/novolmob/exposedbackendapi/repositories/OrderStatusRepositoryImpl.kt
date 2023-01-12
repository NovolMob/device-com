package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.orderStatusByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderStatusCreateModel
import ru.novolmob.backendapi.models.OrderStatusModel
import ru.novolmob.backendapi.models.OrderStatusUpdateModel
import ru.novolmob.backendapi.repositories.IOrderStatusRepository
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.exposeddatabase.entities.OrderStatus

class OrderStatusRepositoryImpl(
    mapper: Mapper<OrderStatus, OrderStatusModel>,
    resultRowMapper: Mapper<ResultRow, OrderStatusModel>,
): IOrderStatusRepository, AbstractCrudRepository<OrderStatusId, OrderStatus.Companion, OrderStatus, OrderStatusModel, OrderStatusCreateModel, OrderStatusUpdateModel>(
    OrderStatus.Companion, mapper, resultRowMapper, ::orderStatusByIdNotFound
) {

    override fun OrderStatus.Companion.new(createModel: OrderStatusCreateModel): Either<AbstractBackendException, OrderStatus> {
        return new {
            this.active = createModel.active
        }.right()
    }

    override fun OrderStatus.applyC(createModel: OrderStatusCreateModel): Either<AbstractBackendException, OrderStatus> {
        return apply {
            this.active = createModel.active
        }.right()
    }

    override fun OrderStatus.applyU(updateModel: OrderStatusUpdateModel): Either<AbstractBackendException, OrderStatus> {
        return apply {
            updateModel.active?.let { this.active = it }
        }.right()
    }
}