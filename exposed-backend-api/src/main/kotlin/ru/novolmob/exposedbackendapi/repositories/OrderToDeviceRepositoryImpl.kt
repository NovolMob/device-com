package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IOrderToDeviceRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.OrderToDeviceEntityId
import ru.novolmob.exposedbackendapi.exceptions.deviceDetailByDeviceIdAndLanguageNotFound
import ru.novolmob.exposeddatabase.entities.Device
import ru.novolmob.exposeddatabase.entities.Order
import ru.novolmob.exposeddatabase.entities.OrderToDeviceEntity
import ru.novolmob.exposeddatabase.tables.OrderToDeviceTable

class OrderToDeviceRepositoryImpl(
    val mapper: Mapper<OrderToDeviceEntity, OrderToDeviceEntityModel>,
    val resultRowMapper: Mapper<ResultRow, OrderToDeviceEntityModel>
): IOrderToDeviceRepository {
    override suspend fun getDevices(orderId: OrderId, language: Language): Either<AbstractBackendException, List<OrderItemModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToDeviceEntity.find { OrderToDeviceTable.order eq orderId }
                .parTraverseEither { orderToDeviceEntity ->
                    val deviceId = orderToDeviceEntity.device.id.value
                    val detail = orderToDeviceEntity.device.details.find { it.language.string == language.string } ?:
                        orderToDeviceEntity.device.details.firstOrNull()
                    detail?.let {
                        OrderItemModel(
                            deviceId = deviceId,
                            title = detail.title,
                            description = detail.description,
                            amount = orderToDeviceEntity.amount,
                            priceForOne = orderToDeviceEntity.priceForOne
                        ).right()
                    } ?: deviceDetailByDeviceIdAndLanguageNotFound(deviceId, language).left()
                }
        }

    override suspend fun removeAllFor(orderId: OrderId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToDeviceEntity.find { OrderToDeviceTable.order eq orderId }
                .parTraverseEither {
                    it.delete().right()
                }.flatMap {
                    true.right()
                }
        }

    override suspend fun get(id: OrderToDeviceEntityId): Either<AbstractBackendException, OrderToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToDeviceEntity.findById(id)?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.orderToDeviceEntityByIdNotFound(
                id
            ).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<OrderToDeviceEntityModel>> =
        RepositoryUtil.generalGetAll(OrderToDeviceTable, pagination, resultRowMapper)

    override suspend fun post(createModel: OrderToDeviceEntityCreateModel): Either<AbstractBackendException, OrderToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = Order.findById(createModel.orderId) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.orderByIdNotFound(
                createModel.orderId
            ).left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound(
                createModel.deviceId
            ).left()
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
    ): Either<AbstractBackendException, OrderToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = Order.findById(createModel.orderId) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.orderByIdNotFound(
                createModel.orderId
            ).left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound(
                createModel.deviceId
            ).left()
            OrderToDeviceEntity.findById(id)?.apply {
                this.order = order
                this.device = device
                this.amount = createModel.amount
                this.priceForOne = createModel.priceForOne
            }?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.orderToDeviceEntityByIdNotFound(id).left()
        }

    override suspend fun put(
        id: OrderToDeviceEntityId,
        updateModel: OrderToDeviceEntityUpdateModel
    ): Either<AbstractBackendException, OrderToDeviceEntityModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = updateModel.orderId?.let {
                Order.findById(it) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.orderByIdNotFound(
                    it
                ).left()
            }
            val device = updateModel.deviceId?.let {
                Device.findById(it) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound(
                    it
                ).left()
            }
            OrderToDeviceEntity.findById(id)?.apply {
                order?.let { this.order = it }
                device?.let { this.device = it }
                updateModel.amount?.let { this.amount = it }
                updateModel.priceForOne?.let { this.priceForOne = it }
            }?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.orderToDeviceEntityByIdNotFound(id).left()
        }

    override suspend fun delete(id: OrderToDeviceEntityId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            OrderToDeviceEntity.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

}