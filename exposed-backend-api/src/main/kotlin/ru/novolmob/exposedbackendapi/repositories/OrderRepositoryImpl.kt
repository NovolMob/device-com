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
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.*
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.exposedbackendapi.exceptions.*
import ru.novolmob.exposeddatabase.entities.*
import ru.novolmob.exposeddatabase.tables.*

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
                    baskets.filter { it.amount.int > it.device.amount.int }.takeIf { it.isNotEmpty() }?.let {
                        return@newSuspendedTransaction notEnoughDevices(it.map { it.device.id.value }).left()
                    }
                    baskets.parTraverseEither {
                        it.device.apply {
                            this.amount = Amount(this.amount.int - it.amount.int)
                            this.updateDate = UpdateTime.now()
                        }.right()
                    }.flatMap {
                        val totalPrice = baskets.sumOf { it.amount.int.toBigDecimal() * it.device.price.bigDecimal }.let(::Price)
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
                                    deviceDetailRepository.getDetailFor(orderToDeviceEntity.device.id.value, language).flatMap {
                                        OrderItemShortModel(
                                            deviceId = it.deviceId,
                                            title = it.title,
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
                            }.flatMap {
                                baskets.parTraverse { it.delete() }
                                it.right()
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
                }.flatMap {
                    it.sortedByDescending { it.creationTime }.right()
                }
        }

    override suspend fun get(id: OrderId): Either<AbstractBackendException, OrderModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Order.findById(id)?.let(mapper::invoke) ?: orderByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<OrderModel>> =
        RepositoryUtil.generalGetAll(Orders, pagination, resultRowMapper)

    override suspend fun post(createModel: OrderCreateModel): Either<AbstractBackendException, OrderModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction userByIdNotFound(createModel.userId).left()
            val point = Point.findById(createModel.pointId) ?: return@newSuspendedTransaction pointByIdNotFound(createModel.pointId).left()
            Order.new {
                this.user = user
                this.point = point
                this.totalCost = createModel.totalCost
            }.let(mapper::invoke)
        }

    override suspend fun post(id: OrderId, createModel: OrderCreateModel): Either<AbstractBackendException, OrderModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction userByIdNotFound(createModel.userId).left()
            val point = Point.findById(createModel.pointId) ?: return@newSuspendedTransaction pointByIdNotFound(createModel.pointId).left()
            Order.findById(id)?.apply {
                this.user = user
                this.point = point
                this.totalCost = createModel.totalCost
            }?.let(mapper::invoke) ?: orderByIdNotFound(id).left()
        }

    override suspend fun put(id: OrderId, updateModel: OrderUpdateModel): Either<AbstractBackendException, OrderModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = updateModel.userId?.let {
                User.findById(it) ?: return@newSuspendedTransaction userByIdNotFound(it).left()
            }
            val point = updateModel.pointId?.let {
                Point.findById(it) ?: return@newSuspendedTransaction pointByIdNotFound(it).left()
            }
            Order.findById(id)?.apply {
                user?.let { this.user = it }
                point?.let { this.point = it }
                updateModel.totalCost?.let { this.totalCost = it }
            }?.let(mapper::invoke) ?: orderByIdNotFound(id).left()
        }

    override suspend fun delete(id: OrderId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Order.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

}