package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.orderByIdNotFound
import ru.novolmob.backend.exceptions.orderStatusByIdNotFound
import ru.novolmob.backend.exceptions.orderToStatusEntityByIdNotFound
import ru.novolmob.backend.exceptions.workerByIdNotFound
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IOrderStatusRepository
import ru.novolmob.backendapi.repositories.IOrderToStatusRepository
import ru.novolmob.exposeddatabase.entities.Order
import ru.novolmob.exposeddatabase.entities.OrderStatus
import ru.novolmob.exposeddatabase.entities.OrderToStatusEntity
import ru.novolmob.exposeddatabase.entities.Worker
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.OrderToStatusEntityId
import ru.novolmob.exposeddatabase.tables.OrderToStatusTable

class OrderToStatusRepositoryImpl(
    val mapper: Mapper<OrderToStatusEntity, OrderToStatusEntityModel>,
    val resultRowMapper: Mapper<ResultRow, OrderToStatusEntityModel>,
    val orderStatusRepository: IOrderStatusRepository
): IOrderToStatusRepository {
    override suspend fun getStatuses(
        orderId: OrderId,
        language: Language
    ): Either<BackendException, List<OrderStatusFullModel>> =
        newSuspendedTransaction {
            OrderToStatusEntity.find { OrderToStatusTable.order eq orderId }
                .sortedByDescending { it.creationDate }
                .parTraverseEither {
                    orderStatusRepository.getFull(it.status.id.value, language)
                }
        }

    override suspend fun get(id: OrderToStatusEntityId): Either<BackendException, OrderToStatusEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToStatusEntity.findById(id)?.let(mapper::invoke) ?: orderToStatusEntityByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<OrderToStatusEntityModel>> =
        RepositoryUtil.generalGatAll(OrderToStatusTable, pagination, resultRowMapper)

    override suspend fun post(createModel: OrderToStatusEntityCreateModel): Either<BackendException, OrderToStatusEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = Order.findById(createModel.orderId) ?: return@newSuspendedTransaction orderByIdNotFound(createModel.orderId).left()
            val status = OrderStatus.findById(createModel.status) ?: return@newSuspendedTransaction orderStatusByIdNotFound(createModel.status).left()
            val worker = Worker.findById(createModel.workerId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.workerId).left()
            OrderToStatusEntity.new {
                this.order = order
                this.status = status
                this.worker = worker
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: OrderToStatusEntityId,
        createModel: OrderToStatusEntityCreateModel
    ): Either<BackendException, OrderToStatusEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = Order.findById(createModel.orderId) ?: return@newSuspendedTransaction orderByIdNotFound(createModel.orderId).left()
            val status = OrderStatus.findById(createModel.status) ?: return@newSuspendedTransaction orderStatusByIdNotFound(createModel.status).left()
            val worker = Worker.findById(createModel.workerId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.workerId).left()
            OrderToStatusEntity.findById(id)?.apply {
                this.order = order
                this.status = status
                this.worker = worker
            }?.let(mapper::invoke) ?: orderToStatusEntityByIdNotFound(id).left()
        }

    override suspend fun put(
        id: OrderToStatusEntityId,
        updateModel: OrderToStatusEntityUpdateModel
    ): Either<BackendException, OrderToStatusEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = updateModel.orderId?.let {
                Order.findById(it) ?: return@newSuspendedTransaction orderByIdNotFound(it).left()
            }
            val status = updateModel.status?.let {
                OrderStatus.findById(it) ?: return@newSuspendedTransaction orderStatusByIdNotFound(it).left()
            }
            val worker = updateModel.workerId?.let {
                Worker.findById(it) ?: return@newSuspendedTransaction workerByIdNotFound(it).left()
            }
            OrderToStatusEntity.findById(id)?.apply {
                order?.let { this.order = it }
                status?.let { this.status = it }
                worker?.let { this.worker = it }
            }?.let(mapper::invoke) ?: orderToStatusEntityByIdNotFound(id).left()
        }

    override suspend fun delete(id: OrderToStatusEntityId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToStatusEntity.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

}