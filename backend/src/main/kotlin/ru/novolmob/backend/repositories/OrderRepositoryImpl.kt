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
import ru.novolmob.backend.exceptions.pointByIdNotFound
import ru.novolmob.backend.exceptions.userByIdNotFound
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.*
import ru.novolmob.database.entities.Order
import ru.novolmob.database.entities.Point
import ru.novolmob.database.entities.User
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.UserId
import ru.novolmob.database.tables.Orders

class OrderRepositoryImpl(
    val mapper: Mapper<Order, OrderModel>,
    val resultRowMapper: Mapper<ResultRow, OrderModel>,
    val pointRepository: IPointRepository,
    val orderToDeviceRepository: IOrderToDeviceRepository,
    val statusRepository: IOrderStatusRepository
): IOrderRepository {
    override suspend fun getFull(orderId: OrderId, language: Language): Either<BackendException, OrderFullModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Order.findById(orderId)?.let { order ->
                pointRepository.getFull(order.point.id.value, language).flatMap { point ->
                    orderToDeviceRepository.getDevices(orderId).flatMap { devices ->
                        statusRepository.getOrderStatus(orderId, language).flatMap { statuses ->
                            OrderFullModel(
                                id = orderId,
                                userId = order.user.id.value,
                                point = point,
                                list = devices,
                                totalCost = order.totalCost,
                                statuses = statuses
                            ).right()
                        }
                    }
                }
            } ?: orderByIdNotFound(orderId).left()
        }

    override suspend fun getOrdersFor(userId: UserId): Either<BackendException, List<OrderModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            Order.find { Orders.user eq userId }.parTraverseEither { mapper(it) }
        }

    override suspend fun get(id: OrderId): Either<BackendException, OrderModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Order.findById(id)?.let(mapper::invoke) ?: orderByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<OrderModel>> =
        RepositoryUtil.generalGatAll(Orders, pagination, resultRowMapper)

    override suspend fun post(createModel: OrderCreateModel): Either<BackendException, OrderModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction userByIdNotFound(createModel.userId).left()
            val point = Point.findById(createModel.pointId) ?: return@newSuspendedTransaction pointByIdNotFound(createModel.pointId).left()
            Order.new {
                this.user = user
                this.point = point
                this.totalCost = createModel.totalCost
            }.let(mapper::invoke)
        }

    override suspend fun post(id: OrderId, createModel: OrderCreateModel): Either<BackendException, OrderModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction userByIdNotFound(createModel.userId).left()
            val point = Point.findById(createModel.pointId) ?: return@newSuspendedTransaction pointByIdNotFound(createModel.pointId).left()
            Order.findById(id)?.apply {
                this.user = user
                this.point = point
                this.totalCost = createModel.totalCost
            }?.let(mapper::invoke) ?: orderByIdNotFound(id).left()
        }

    override suspend fun put(id: OrderId, updateModel: OrderUpdateModel): Either<BackendException, OrderModel> =
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

    override suspend fun delete(id: OrderId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Order.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

}