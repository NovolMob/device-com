package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.basketByIdNotFoundException
import ru.novolmob.backend.exceptions.deviceByIdNotFound
import ru.novolmob.backend.exceptions.userByIdNotFound
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IBasketRepository
import ru.novolmob.database.entities.Basket
import ru.novolmob.database.entities.Device
import ru.novolmob.database.entities.User
import ru.novolmob.database.models.Amount
import ru.novolmob.database.models.UpdateDate
import ru.novolmob.database.models.ids.BasketId
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.UserId
import ru.novolmob.database.tables.Baskets

class BasketRepositoryImpl(
    val mapper: Mapper<Basket, BasketModel>,
    val resultRowMapper: Mapper<ResultRow, BasketModel>,
    val itemMapper: Mapper<Basket, BasketItemModel>
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
                    this.updateDate = UpdateDate.now()
                    true.right()
                } ?: let {
                    post(BasketCreateModel(userId, deviceId, amount)).flatMap {
                        false.right()
                    }
                }
            } else if (basketItem != null) {
                removeFromBasket(userId, deviceId)
            } else false.right()
        }

    override suspend fun removeFromBasket(userId: UserId, deviceId: DeviceId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            find(userId, deviceId)?.run {
                delete()
                true.right()
            } ?: false.right()
        }

    override suspend fun getAmount(userId: UserId, deviceId: DeviceId): Either<BackendException, Amount> =
        newSuspendedTransaction(Dispatchers.IO) {
            (find(userId, deviceId)?.amount ?: Amount(0)).right()
        }

    override suspend fun get(id: BasketId): Either<BackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Basket.findById(id)?.let(mapper::invoke) ?: basketByIdNotFoundException(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<BasketModel>> =
        RepositoryUtil.generalGatAll(Baskets, pagination, resultRowMapper)

    override suspend fun post(createModel: BasketCreateModel): Either<BackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction userByIdNotFound(createModel.userId).left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction deviceByIdNotFound(createModel.deviceId).left()
            Basket.new {
                this.user = user
                this.device = device
                this.amount = createModel.amount
            }.let(mapper::invoke)
        }

    override suspend fun post(id: BasketId, createModel: BasketCreateModel): Either<BackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction userByIdNotFound(createModel.userId).left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction deviceByIdNotFound(createModel.deviceId).left()
            Basket.findById(id)?.apply {
                this.user = user
                this.device = device
                this.amount = createModel.amount
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: basketByIdNotFoundException(id).left()
        }

    override suspend fun put(id: BasketId, updateModel: BasketUpdateModel): Either<BackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {

            val user = updateModel.userId?.let {
                User.findById(it) ?: return@newSuspendedTransaction userByIdNotFound(it).left()
            }
            val device = updateModel.deviceId?.let {
                Device.findById(it) ?: return@newSuspendedTransaction deviceByIdNotFound(it).left()
            }
            Basket.findById(id)?.apply {
                user?.let { this.user = it }
                device?.let { this.device = it }
                updateModel.amount?.let { this.amount = it }
                this.updateDate = UpdateDate.now()
            }?.let(mapper::invoke) ?: basketByIdNotFoundException(id).left()
        }

    override suspend fun delete(id: BasketId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Basket.findById(id)?.run {
                delete()
                true.right()
            } ?: false.right()
        }
}