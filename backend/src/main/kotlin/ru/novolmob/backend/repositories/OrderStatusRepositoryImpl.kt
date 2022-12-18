package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.orderStatusByIdNotFound
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IOrderStatusDetailRepository
import ru.novolmob.backendapi.repositories.IOrderStatusRepository
import ru.novolmob.database.entities.OrderStatus
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.OrderStatusId
import ru.novolmob.database.tables.OrderStatuses

class OrderStatusRepositoryImpl(
    val mapper: Mapper<OrderStatus, OrderStatusModel>,
    val resultRowMapper: Mapper<ResultRow, OrderStatusModel>,
    val orderStatusDetailRepository: IOrderStatusDetailRepository
): IOrderStatusRepository {
    override suspend fun get(id: OrderStatusId): Either<BackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.findById(id)?.let(mapper::invoke) ?: orderStatusByIdNotFound(id).left()
        }

    override suspend fun getFull(
        orderStatusId: OrderStatusId,
        language: Language
    ): Either<BackendException, OrderStatusFullModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.findById(orderStatusId)?.let { orderStatus ->
                orderStatusDetailRepository.getDetailFor(orderStatusId, language).flatMap { detail ->
                    OrderStatusFullModel(
                        id = orderStatusId,
                        active = orderStatus.active,
                        detail = detail
                    ).right()
                }
            } ?: orderStatusByIdNotFound(orderStatusId).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<OrderStatusModel>> =
        RepositoryUtil.generalGatAll(OrderStatuses, pagination, resultRowMapper)

    override suspend fun post(createModel: OrderStatusCreateModel): Either<BackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.new {
                this.active = createModel.active
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: OrderStatusId,
        createModel: OrderStatusCreateModel
    ): Either<BackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.findById(id)?.apply {
                this.active = createModel.active
            }?.let(mapper::invoke) ?: orderStatusByIdNotFound(id).left()
        }

    override suspend fun put(
        id: OrderStatusId,
        updateModel: OrderStatusUpdateModel
    ): Either<BackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.findById(id)?.apply {
                updateModel.active?.let { this.active = it }
            }?.let(mapper::invoke) ?: orderStatusByIdNotFound(id).left()
        }

    override suspend fun delete(id: OrderStatusId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}