package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.*
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderStatusFullModel
import ru.novolmob.backendapi.models.OrderStatusShortModel
import ru.novolmob.backendapi.models.OrderToStatusEntityCreateModel
import ru.novolmob.backendapi.models.OrderToStatusEntityModel
import ru.novolmob.backendapi.repositories.IOrderStatusDetailRepository
import ru.novolmob.backendapi.repositories.IOrderToStatusRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.exposeddatabase.entities.Order
import ru.novolmob.exposeddatabase.entities.OrderStatus
import ru.novolmob.exposeddatabase.entities.OrderToStatusEntity
import ru.novolmob.exposeddatabase.entities.Worker
import ru.novolmob.exposeddatabase.tables.OrderToStatusTable

class OrderToStatusRepositoryImpl(
    val mapper: Mapper<OrderToStatusEntity, OrderToStatusEntityModel>,
    val resultRowMapper: Mapper<ResultRow, OrderToStatusEntityModel>,
    val orderStatusDetailRepository: IOrderStatusDetailRepository
): IOrderToStatusRepository {
    override suspend fun getStatuses(
        orderId: OrderId,
        language: Language
    ): Either<AbstractBackendException, List<OrderStatusFullModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToStatusEntity.find { OrderToStatusTable.order eq orderId }
                .sortedByDescending { it.creationDate }
                .parTraverseEither {
                    OrderStatus.findById(it.status.id.value)?.let { orderStatus ->
                        orderStatusDetailRepository.getDetailFor(it.status.id.value, language).flatMap { detail ->
                            OrderStatusFullModel(
                                id = orderStatus.id.value,
                                active = orderStatus.active,
                                detail = detail,
                                dateTime = it.creationDate
                            ).right()
                        }
                    } ?: orderStatusByIdNotFound(it.status.id.value).left()
                }
        }

    override suspend fun getLastStatus(
        orderId: OrderId,
        language: Language
    ): Either<AbstractBackendException, OrderStatusShortModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToStatusEntity.find { OrderToStatusTable.order eq orderId }
                .orderBy(OrderToStatusTable.creationDate to SortOrder.DESC)
                .limit(1).firstOrNull()
                ?.let {
                    val orderStatusId = it.status.id.value
                    OrderStatus.findById(orderStatusId)?.let { orderStatus ->
                        orderStatusDetailRepository.getDetailFor(orderStatusId, language).flatMap { detail ->
                            OrderStatusShortModel(
                                id = orderStatusId,
                                active = orderStatus.active,
                                title = detail.title,
                                description = detail.description,
                                dateTime = it.creationDate
                            ).right()
                        }
                    } ?: orderStatusByIdNotFound(orderStatusId).left()
                } ?: orderStatusByOrderIdNotFound(orderId).left()
        }

    override suspend fun post(createModel: OrderToStatusEntityCreateModel): Either<AbstractBackendException, Unit> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = Order.findById(createModel.orderId)
                ?: return@newSuspendedTransaction orderByIdNotFound(createModel.orderId).left()
            val status = OrderStatus.findById(createModel.status)
                ?: return@newSuspendedTransaction orderStatusByIdNotFound(createModel.status).left()
            val worker = Worker.findById(createModel.workerId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.workerId).left()
            OrderToStatusEntity.new {
                this.order = order
                this.status = status
                this.worker = worker
            }
            Unit.right()
        }

    override suspend fun delete(orderId: OrderId, orderStatusId: OrderStatusId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToStatusEntity.find { (OrderToStatusTable.order eq orderId) and (OrderToStatusTable.orderStatus eq orderStatusId) }
                .singleOrNull()?.let {
                    it.delete()
                    true.right()
                } ?: false.right()
        }

}