package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IOrderStatusRepository
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.exposeddatabase.entities.OrderStatus
import ru.novolmob.exposeddatabase.tables.OrderStatuses

class OrderStatusRepositoryImpl(
    val mapper: Mapper<OrderStatus, OrderStatusModel>,
    val resultRowMapper: Mapper<ResultRow, OrderStatusModel>,
): IOrderStatusRepository {
    override suspend fun get(id: OrderStatusId): Either<AbstractBackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.findById(id)?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.orderStatusByIdNotFound(
                id
            ).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<OrderStatusModel>> =
        RepositoryUtil.generalGetAll(OrderStatuses, pagination, resultRowMapper)

    override suspend fun post(createModel: OrderStatusCreateModel): Either<AbstractBackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.new {
                this.active = createModel.active
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: OrderStatusId,
        createModel: OrderStatusCreateModel
    ): Either<AbstractBackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.findById(id)?.apply {
                this.active = createModel.active
            }?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.orderStatusByIdNotFound(id).left()
        }

    override suspend fun put(
        id: OrderStatusId,
        updateModel: OrderStatusUpdateModel
    ): Either<AbstractBackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.findById(id)?.apply {
                updateModel.active?.let { this.active = it }
            }?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.orderStatusByIdNotFound(id).left()
        }

    override suspend fun delete(id: OrderStatusId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}