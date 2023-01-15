package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.*
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderFullModel
import ru.novolmob.backendapi.models.OrderItemShortModel
import ru.novolmob.backendapi.models.OrderModel
import ru.novolmob.backendapi.models.OrderShortModel
import ru.novolmob.backendapi.repositories.*
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Price.Companion.sumOf
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.exposeddatabase.entities.*
import ru.novolmob.exposeddatabase.tables.Baskets
import ru.novolmob.exposeddatabase.tables.OrderToDeviceTable
import ru.novolmob.exposeddatabase.tables.OrderToStatusTable
import ru.novolmob.exposeddatabase.tables.Orders

class OrderRepositoryImpl(
    val mapper: Mapper<Order, OrderModel>,
    val resultRowMapper: Mapper<ResultRow, OrderModel>,
    val deviceDetailRepository: IDeviceDetailRepository,
    val pointRepository: IPointRepository,
    val orderToDeviceRepository: IOrderToDeviceRepository,
    val orderToStatusRepository: IOrderToStatusRepository
): IOrderRepository {
    override suspend fun confirmOrder(
        userId: UserId,
        pointId: PointId,
        language: Language
    ): Either<AbstractBackendException, OrderShortModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Basket.find { Baskets.user eq userId }
                .let { baskets ->
                    val user = User.findById(userId) ?: return@newSuspendedTransaction userByIdNotFound(userId).left()
                    val point = Point.findById(pointId) ?: return@newSuspendedTransaction pointByIdNotFound(pointId).left()
                    baskets.filter { it.amount.int > it.device.amount.int }.takeIf { it.isNotEmpty() }?.let { list ->
                        return@newSuspendedTransaction notEnoughDevices(list.map { it.device.id.value }).left()
                    }
                    baskets.parTraverseEither {
                        it.device.apply {
                            this.amount = Amount(this.amount.int - it.amount.int)
                            this.updateDate = UpdateTime.now()
                        }.right()
                    }.flatMap {
                        val totalPrice = baskets.sumOf { it.device.price * it.amount.int}
                        Order.new {
                            this.user = user
                            this.point = point
                            this.totalCost = totalPrice
                        }.let { order ->
                            baskets.parTraverseEither {
                                OrderToDeviceEntity.new {
                                    this.order = Order.findById(order.id.value)!!
                                    this.device = it.device
                                    this.amount = it.amount
                                    this.priceForOne = it.device.price
                                }.right()
                            }.flatMap {
                                it.parTraverseEither { orderToDeviceEntity ->
                                    deviceDetailRepository.getDetailFor(orderToDeviceEntity.device.id.value, language).flatMap { deviceDetailModel ->
                                        OrderItemShortModel(
                                            deviceId = deviceDetailModel.deviceId,
                                            title = deviceDetailModel.title,
                                            amount = orderToDeviceEntity.amount,
                                            priceForOne = orderToDeviceEntity.priceForOne
                                        ).right()
                                    }
                                }.flatMap { devices ->
                                    pointRepository.getShort(pointId, language).flatMap { pointFullModel ->
                                        OrderShortModel(
                                            id = order.id.value,
                                            point = pointFullModel.address,
                                            list = devices,
                                            totalCost = order.totalCost,
                                            status = null,
                                            active = true,
                                            creationTime = order.creationDate
                                        ).right()
                                    }
                                }
                            }.flatMap { orderShortModel ->
                                baskets.parTraverse { it.delete() }
                                orderShortModel.right()
                            }
                        }
                    }
                }
        }

    override suspend fun cancelOrder(orderId: OrderId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Order.findById(orderId)?.let { order ->
                OrderToStatusEntity.find { OrderToStatusTable.order eq orderId }
                    .orderBy(OrderToStatusTable.creationDate to SortOrder.DESC)
                    .limit(1).firstOrNull()?.let {
                        if (!it.status.active) return@newSuspendedTransaction cantCancelOrder(orderId).left()
                    }
                val orderToDeviceEntities = OrderToDeviceEntity.find { OrderToDeviceTable.order eq orderId }
                orderToDeviceEntities
                    .parTraverseEither {
                        it.device.apply {
                            this.amount = Amount(this.amount.int + it.amount.int)
                            this.updateDate = UpdateTime.now()
                        }.right()
                    }.flatMap {
                        OrderToStatusEntity.find { OrderToStatusTable.order eq orderId }
                            .parTraverseEither { it.delete().right() }
                            .flatMap {
                                orderToDeviceEntities.parTraverse { it.delete() }.right()
                            }
                            .flatMap {
                                order.delete()
                                true.right()
                            }
                    }
            } ?: orderByIdNotFound(orderId).left()
        }

    override suspend fun getFull(orderId: OrderId, language: Language): Either<AbstractBackendException, OrderFullModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Order.findById(orderId)?.let { order ->
                pointRepository.getFull(order.point.id.value, language).flatMap { point ->
                    orderToDeviceRepository.getDevices(orderId, language).flatMap { devices ->
                        OrderFullModel(
                            id = orderId,
                            userId = order.user.id.value,
                            point = point,
                            list = devices,
                            totalCost = order.totalCost,
                            lastStatus = orderToStatusRepository.getLastStatus(orderId, language).orNull(),
                            creationTime = order.creationDate
                        ).right()
                    }
                }
            } ?: orderByIdNotFound(orderId).left()
        }

    override suspend fun getOrdersFor(userId: UserId, language: Language): Either<AbstractBackendException, List<OrderShortModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            Order.find { Orders.user eq userId }
                .parTraverseEither { order ->
                    val orderId = order.id.value
                    pointRepository.getShort(order.point.id.value, language).flatMap { point ->
                        orderToDeviceRepository.getDevices(order.id.value, language).flatMap { devices ->
                            val status = orderToStatusRepository.getLastStatus(order.id.value, language).orNull()
                            OrderShortModel(
                                id = orderId,
                                point = point.address,
                                list = devices.map {
                                   OrderItemShortModel(
                                       deviceId = it.deviceId,
                                       title = it.title,
                                       amount = it.amount,
                                       priceForOne = it.priceForOne
                                   )
                                },
                                totalCost = order.totalCost,
                                status = status?.title,
                                active = status?.active ?: true,
                                creationTime = order.creationDate
                            ).right()
                        }
                    }
                }.flatMap { list ->
                    list.sortedByDescending { it.creationTime }.right()
                }
        }

}