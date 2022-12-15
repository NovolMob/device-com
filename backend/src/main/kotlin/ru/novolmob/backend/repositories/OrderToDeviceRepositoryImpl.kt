package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.deviceByIdNotFound
import ru.novolmob.backend.exceptions.orderByIdNotFound
import ru.novolmob.backend.exceptions.orderToDeviceEntityByIdNotFound
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IOrderToDeviceRepository
import ru.novolmob.database.entities.Device
import ru.novolmob.database.entities.Order
import ru.novolmob.database.entities.OrderToDeviceEntity
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.OrderToDeviceEntityId
import ru.novolmob.database.tables.OrderToDeviceTable

class OrderToDeviceRepositoryImpl(
    val mapper: Mapper<OrderToDeviceEntity, OrderToDeviceEntityModel>,
    val itemMapper: Mapper<OrderToDeviceEntity, OrderItemModel>,
    val resultRowMapper: Mapper<ResultRow, OrderToDeviceEntityModel>,
): IOrderToDeviceRepository {
    override suspend fun getDevices(orderId: OrderId): Either<BackendException, List<OrderItemModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToDeviceEntity.find { OrderToDeviceTable.order eq orderId }
                .parTraverseEither { itemMapper(it) }
        }

    override suspend fun get(id: OrderToDeviceEntityId): Either<BackendException, OrderToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToDeviceEntity.findById(id)?.let(mapper::invoke) ?: orderToDeviceEntityByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<OrderToDeviceEntityModel>> =
        RepositoryUtil.generalGatAll(OrderToDeviceTable, pagination, resultRowMapper)

    override suspend fun post(createModel: OrderToDeviceEntityCreateModel): Either<BackendException, OrderToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = Order.findById(createModel.orderId) ?: return@newSuspendedTransaction orderByIdNotFound(createModel.orderId).left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction deviceByIdNotFound(createModel.deviceId).left()
            OrderToDeviceEntity.new {
                this.order = order
                this.device = device
                this.amount = createModel.amount
                this.priceForOne = createModel.priceForOne
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: OrderToDeviceEntityId,
        createModel: OrderToDeviceEntityCreateModel
    ): Either<BackendException, OrderToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = Order.findById(createModel.orderId) ?: return@newSuspendedTransaction orderByIdNotFound(createModel.orderId).left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction deviceByIdNotFound(createModel.deviceId).left()
            OrderToDeviceEntity.findById(id)?.apply {
                this.order = order
                this.device = device
                this.amount = createModel.amount
                this.priceForOne = createModel.priceForOne
            }?.let(mapper::invoke) ?: orderToDeviceEntityByIdNotFound(id).left()
        }

    override suspend fun put(
        id: OrderToDeviceEntityId,
        updateModel: OrderToDeviceEntityUpdateModel
    ): Either<BackendException, OrderToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = updateModel.orderId?.let {
                Order.findById(it) ?: return@newSuspendedTransaction orderByIdNotFound(it).left()
            }
            val device = updateModel.deviceId?.let {
                Device.findById(it) ?: return@newSuspendedTransaction deviceByIdNotFound(it).left()
            }
            OrderToDeviceEntity.findById(id)?.apply {
                order?.let { this.order = it }
                device?.let { this.device = it }
                updateModel.amount?.let { this.amount = it }
                updateModel.priceForOne?.let { this.priceForOne = it }
            }?.let(mapper::invoke) ?: orderToDeviceEntityByIdNotFound(id).left()
        }

    override suspend fun delete(id: OrderToDeviceEntityId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToDeviceEntity.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

}