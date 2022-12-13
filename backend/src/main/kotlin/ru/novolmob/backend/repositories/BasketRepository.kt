package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IBasketRepository
import ru.novolmob.database.entities.Basket
import ru.novolmob.database.entities.Device
import ru.novolmob.database.entities.User
import ru.novolmob.database.models.Amount
import ru.novolmob.database.models.CreationDate
import ru.novolmob.database.models.UpdateDate
import ru.novolmob.database.models.ids.BasketId
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.UserId
import ru.novolmob.database.tables.Baskets

class BasketRepository(
    val mapper: Mapper<Basket, BasketModel>,
    val resultRowMapper: Mapper<ResultRow, BasketModel>,
    val itemMapper: Mapper<Basket, BasketItemModel>,
): IBasketRepository {

    private fun find(userId: UserId, deviceId: DeviceId): Basket? =
        Basket.find { (Baskets.user eq userId) and (Baskets.device eq deviceId) }.limit(1).firstOrNull()
    override suspend fun getBasket(userId: UserId): Either<BackendException, List<BasketItemModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            Basket.find { Baskets.user eq userId }.parTraverseEither { itemMapper(it) }
        }

    override suspend fun setInBasket(
        userId: UserId,
        deviceId: DeviceId,
        amount: Amount
    ): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            val basketItem = find(userId, deviceId)
            if (amount.int > 0) {
                basketItem?.run {
                    this.amount = amount
                    this.updateDate = UpdateDate(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
                    true.right()
                } ?: let {
                    val user = User.findById(userId) ?: return@newSuspendedTransaction BackendException(code = BackendExceptionCode.UNKNOWN, message = "User is not exists!").left()
                    val device = Device.findById(deviceId) ?: return@newSuspendedTransaction BackendException(code = BackendExceptionCode.UNKNOWN, message = "Device is not exists!").left()
                    Basket.new {
                        this.user = user
                        this.device = device
                        this.amount = amount
                        this.updateDate = UpdateDate(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
                        this.creationDate = CreationDate(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
                    }
                    false.right()
                }
            } else if (basketItem != null) {
                removeFromBasket(userId, deviceId, amount)
                true.right()
            } else false.right()
        }

    override suspend fun removeFromBasket(userId: UserId, deviceId: DeviceId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            find(userId, deviceId)?.run {
                delete()
                true.right()
            } ?: false.right()
        }

    override suspend fun removeFromBasket(
        userId: UserId,
        deviceId: DeviceId,
        amount: Amount
    ): Either<BackendException, Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAmount(userId: UserId, deviceId: DeviceId): Either<BackendException, Amount> =
        newSuspendedTransaction(Dispatchers.IO) {
            find(userId, deviceId)?.amount?.right()
                ?: BackendException(code = BackendExceptionCode.UNKNOWN, message = "User $userId don`t have device $deviceId in basket!").left()
        }

    override suspend fun get(id: BasketId): Either<BackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Basket.findById(id)?.let(mapper::invoke) ?: BackendException(code = BackendExceptionCode.UNKNOWN, message = "Basket row with id $id not found!").left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<BasketModel>> =
        newSuspendedTransaction {
            val column = pagination.sortByColumn?.let { sortByColumn ->
                Baskets.columns.singleOrNull { it.name == sortByColumn } ?: return@newSuspendedTransaction BackendException(code = BackendExceptionCode.UNKNOWN, message = "Basket table don`t have a column called $sortByColumn!").left()
            }
            val sortOrder = pagination.sortOrder?.let { sortOrder ->
                kotlin.runCatching { SortOrder.valueOf(sortOrder) }.getOrNull()
                    ?: return@newSuspendedTransaction BackendException(code = BackendExceptionCode.UNKNOWN, message = "SortOrder $sortOrder not exists!").left()
            }
            val page = pagination.page ?: 0
            Baskets
                .selectAll()
                .let { query ->
                    column?.let { col ->
                        sortOrder?.let {
                            query.orderBy(col, it)
                        } ?: query.orderBy(col)
                    } ?: query
                }
                .let { query ->
                    pagination.pageSize?.let { size ->
                        query.limit(size.toInt(), size * page)
                    } ?: query
                }.parTraverseEither { resultRowMapper(it) }
                .flatMap {
                    Page(page = page, size = it.size.toLong(), it).right()
                }
        }

    override suspend fun post(createModel: BasketCreateModel): Either<BackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction BackendException(code = BackendExceptionCode.UNKNOWN, message = "User is not exists!").left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction BackendException(code = BackendExceptionCode.UNKNOWN, message = "Device is not exists!").left()
            Basket.new {
                this.user = user
                this.device = device
                this.amount = createModel.amount
            }.let(mapper::invoke)
        }

    override suspend fun post(id: BasketId, createModel: BasketCreateModel): Either<BackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction BackendException(code = BackendExceptionCode.UNKNOWN, message = "User is not exists!").left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction BackendException(code = BackendExceptionCode.UNKNOWN, message = "Device is not exists!").left()
            Basket.findById(id)?.apply {
                this.user = user
                this.device = device
                this.amount = createModel.amount
            }?.let(mapper::invoke) ?: BackendException(code = BackendExceptionCode.UNKNOWN, message = "Basket row with id $id not found!").left()
        }

    override suspend fun put(id: BasketId, updateModel: BasketUpdateModel): Either<BackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {

            val user = updateModel.userId?.let {
                User.findById(it) ?: return@newSuspendedTransaction BackendException(code = BackendExceptionCode.UNKNOWN, message = "User is not exists!").left()
            }
            val device = updateModel.deviceId?.let {
                Device.findById(it) ?: return@newSuspendedTransaction BackendException(code = BackendExceptionCode.UNKNOWN, message = "Device is not exists!").left()
            }
            Basket.findById(id)?.apply {
                user?.let { this.user = it }
                device?.let { this.device = it }
                updateModel.amount?.let { this.amount = it }
                updateDate = UpdateDate(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
            }?.let(mapper::invoke) ?: BackendException(code = BackendExceptionCode.UNKNOWN, message = "Basket row with id $id not found!").left()
        }

    override suspend fun delete(id: BasketId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Basket.findById(id)?.run {
                delete()
                true.right()
            } ?: false.right()
        }
}