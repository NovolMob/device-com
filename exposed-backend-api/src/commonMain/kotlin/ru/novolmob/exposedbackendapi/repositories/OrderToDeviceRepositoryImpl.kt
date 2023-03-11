package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.deviceByIdNotFound
import ru.novolmob.backendapi.exceptions.deviceDetailByDeviceIdAndLanguageNotFound
import ru.novolmob.backendapi.exceptions.orderByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderItemModel
import ru.novolmob.backendapi.models.OrderToDeviceEntityCreateModel
import ru.novolmob.backendapi.models.OrderToDeviceEntityModel
import ru.novolmob.backendapi.repositories.IOrderToDeviceRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
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

    override suspend fun post(createModel: OrderToDeviceEntityCreateModel): Either<AbstractBackendException, Unit> =
        newSuspendedTransaction(Dispatchers.IO) {
            val order = Order.findById(createModel.orderId)
                ?: return@newSuspendedTransaction orderByIdNotFound(createModel.orderId).left()
            val device = Device.findById(createModel.deviceId)
                ?: return@newSuspendedTransaction deviceByIdNotFound(createModel.deviceId).left()
            OrderToDeviceEntity.new {
                this.order = order
                this.device = device
                this.amount = createModel.amount
                this.priceForOne = createModel.priceForOne
            }
            Unit.right()
        }

}