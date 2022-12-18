package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IOrderStatusDetailRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateDate
import ru.novolmob.core.models.ids.OrderStatusDetailId
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.exposeddatabase.entities.OrderStatus
import ru.novolmob.exposeddatabase.entities.OrderStatusDetail
import ru.novolmob.exposeddatabase.tables.OrderStatusDetails

class OrderStatusDetailRepositoryImpl(
    val mapper: Mapper<OrderStatusDetail, OrderStatusDetailModel>,
    val resultRowMapper: Mapper<ResultRow, OrderStatusDetailModel>
): IOrderStatusDetailRepository {
    override suspend fun getDetailFor(
        orderStatusId: OrderStatusId,
        language: Language
    ): Either<BackendException, OrderStatusDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatusDetail
                .find { (OrderStatusDetails.orderStatus eq orderStatusId) and (OrderStatusDetails.language eq language) }
                .limit(1).firstOrNull()?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.orderStatusDetailByDeviceIdAndLanguageNotFound(
                orderStatusId,
                language
            ).left()
        }

    override suspend fun removeDetailFor(orderStatusId: OrderStatusId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatusDetail.find { OrderStatusDetails.orderStatus eq orderStatusId }
                .takeIf { !it.empty() }?.let {
                    it.parTraverse { it.delete() }
                    true.right()
                } ?: false.right()
        }

    override suspend fun get(id: OrderStatusDetailId): Either<BackendException, OrderStatusDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatusDetail.findById(id)?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.orderStatusDetailByIdNotFound(
                id
            ).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<OrderStatusDetailModel>> =
        RepositoryUtil.generalGatAll(OrderStatusDetails, pagination, resultRowMapper)

    override suspend fun post(createModel: OrderStatusDetailCreateModel): Either<BackendException, OrderStatusDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val orderStatus = OrderStatus.findById(createModel.orderStatusId) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.orderStatusByIdNotFound(
                createModel.orderStatusId
            ).left()
            OrderStatusDetail.new {
                this.orderStatus = orderStatus
                this.title = createModel.title
                this.description = createModel.description
                this.language = language
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: OrderStatusDetailId,
        createModel: OrderStatusDetailCreateModel
    ): Either<BackendException, OrderStatusDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val orderStatus = OrderStatus.findById(createModel.orderStatusId) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.orderStatusByIdNotFound(
                createModel.orderStatusId
            ).left()
            OrderStatusDetail.findById(id)?.apply {
                this.orderStatus = orderStatus
                this.title = createModel.title
                this.description = createModel.description
                this.language = language
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.orderStatusDetailByIdNotFound(id).left()
        }

    override suspend fun put(
        id: OrderStatusDetailId,
        updateModel: OrderStatusDetailUpdateModel
    ): Either<BackendException, OrderStatusDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val orderStatus = updateModel.orderStatusId?.let {
                OrderStatus.findById(it) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.orderStatusByIdNotFound(
                    it
                ).left()
            }
            OrderStatusDetail.findById(id)?.apply {
                orderStatus?.let { this.orderStatus = it }
                updateModel.title?.let { this.title = it }
                updateModel.description?.let { this.description = it }
                updateModel.language?.let { this.language = it }
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.orderStatusDetailByIdNotFound(id).left()
        }

    override suspend fun delete(id: OrderStatusDetailId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderStatusDetail.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}