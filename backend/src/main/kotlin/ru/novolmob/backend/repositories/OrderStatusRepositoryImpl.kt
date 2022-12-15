package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.orderByIdNotFound
import ru.novolmob.backend.exceptions.orderStatusByIdNotFound
import ru.novolmob.backend.exceptions.orderStatusByOrderIdNotFound
import ru.novolmob.backend.exceptions.workerByIdNotFound
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IOrderStatusDetailRepository
import ru.novolmob.backendapi.repositories.IOrderStatusRepository
import ru.novolmob.database.entities.Order
import ru.novolmob.database.entities.OrderStatus
import ru.novolmob.database.entities.Worker
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.OrderStatusId
import ru.novolmob.database.tables.OrderStatuses

class OrderStatusRepositoryImpl(
    val mapper: Mapper<OrderStatus, OrderStatusModel>,
    val resultRowMapper: Mapper<ResultRow, OrderStatusModel>,
    val orderStatusDetailRepository: IOrderStatusDetailRepository
): IOrderStatusRepository {
    override suspend fun getFull(
        orderStatusId: OrderStatusId,
        language: Language
    ): Either<BackendException, OrderStatusFullModel> =
         newSuspendedTransaction(Dispatchers.IO) {
             OrderStatus.findById(orderStatusId)?.let { orderStatus ->
                 orderStatusDetailRepository.getDetailFor(orderStatusId, language).flatMap { detail ->
                     OrderStatusFullModel(
                         id = orderStatusId,
                         orderId = orderStatus.order.id.value,
                         workerId = orderStatus.worker.id.value,
                         detail = detail
                     ).right()
                 }
             } ?: orderStatusByIdNotFound(orderStatusId).left()
         }

    override suspend fun getOrderStatus(orderId: OrderId, language: Language): Either<BackendException, List<OrderStatusFullModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.find {OrderStatuses.order eq orderId}
                .takeIf { !it.empty() }?.let {
                    it.parTraverseEither {orderStatus ->
                        orderStatusDetailRepository.getDetailFor(orderStatus.id.value, language).flatMap { detail ->
                            OrderStatusFullModel(
                                id = orderStatus.id.value,
                                orderId = orderStatus.order.id.value,
                                workerId = orderStatus.worker.id.value,
                                detail = detail
                            ).right()
                        }
                    }
                } ?: orderStatusByOrderIdNotFound(orderId).left()
        }

    override suspend fun get(id: OrderStatusId): Either<BackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatus.findById(id)?.let(mapper::invoke) ?: orderStatusByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<OrderStatusModel>> =
        RepositoryUtil.generalGatAll(OrderStatuses, pagination, resultRowMapper)

    override suspend fun post(createModel: OrderStatusCreateModel): Either<BackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = Order.findById(createModel.orderId) ?: return@newSuspendedTransaction orderByIdNotFound(createModel.orderId).left()
            val worker = Worker.findById(createModel.workerId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.workerId).left()
            OrderStatus.new {
                this.order = order
                this.worker = worker
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: OrderStatusId,
        createModel: OrderStatusCreateModel
    ): Either<BackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = Order.findById(createModel.orderId) ?: return@newSuspendedTransaction orderByIdNotFound(createModel.orderId).left()
            val worker = Worker.findById(createModel.workerId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.workerId).left()
            OrderStatus.findById(id)?.apply {
                this.order = order
                this.worker = worker
            }?.let(mapper::invoke) ?: orderStatusByIdNotFound(id).left()
        }

    override suspend fun put(
        id: OrderStatusId,
        updateModel: OrderStatusUpdateModel
    ): Either<BackendException, OrderStatusModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = updateModel.orderId?.let {
                Order.findById(it) ?: return@newSuspendedTransaction orderByIdNotFound(it).left()
            }
            val worker = updateModel.workerId?.let {
                Worker.findById(it) ?: return@newSuspendedTransaction workerByIdNotFound(it).left()
            }
            OrderStatus.findById(id)?.apply {
                order?.let { this.order = it }
                worker?.let { this.worker = it }
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