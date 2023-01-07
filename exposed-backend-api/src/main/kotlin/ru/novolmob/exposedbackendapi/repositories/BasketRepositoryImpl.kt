package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IBasketRepository
import ru.novolmob.backendapi.repositories.IDeviceDetailRepository
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.BasketId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.exposedbackendapi.exceptions.userByIdNotFound
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.exposeddatabase.entities.Basket
import ru.novolmob.exposeddatabase.entities.Device
import ru.novolmob.exposeddatabase.entities.User
import ru.novolmob.exposeddatabase.tables.Baskets

class BasketRepositoryImpl(
    val mapper: Mapper<Basket, BasketModel>,
    val resultRowMapper: Mapper<ResultRow, BasketModel>,
    val deviceDetailRepository: IDeviceDetailRepository,
): IBasketRepository {

    private fun find(userId: UserId, deviceId: DeviceId): Basket? =
        Basket.find { (Baskets.user eq userId) and (Baskets.device eq deviceId) }.limit(1).firstOrNull()
    override suspend fun getBasket(userId: UserId, language: Language): Either<AbstractBackendException, BasketFullModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Basket.find { Baskets.user eq userId }
                .sortedByDescending { it.creationTime }
                .let { baskets ->
                    baskets.parTraverseEither { basket ->
                        deviceDetailRepository.getDetailFor(basket.device.id.value, language).flatMap {
                            BasketItemModel(
                                device = DeviceShortModel(
                                    id = it.deviceId,
                                    title = it.title,
                                    description = it.description,
                                    amount = basket.device.amount,
                                    price = basket.device.price
                                ),
                                amount = basket.amount,
                            ).right()
                        }
                    }.flatMap { list ->
                        BasketFullModel(
                            list = list,
                            totalPrice = Price(list.sumOf { product -> product.amount.int.toBigDecimal() * product.device.price.bigDecimal })
                        ).right()
                    }
                }
        }

    override suspend fun setInBasket(
        userId: UserId,
        deviceId: DeviceId,
        amount: Amount
    ): Either<AbstractBackendException, Price> =
        newSuspendedTransaction(Dispatchers.IO) {
            val basketItem = find(userId, deviceId)
            if (amount.int > 0) {
                basketItem?.run {
                    this.amount = amount
                    this.updateTime = UpdateTime.now()
                } ?: post(BasketCreateModel(userId, deviceId, amount))
            } else if (basketItem != null) {
                removeFromBasket(userId, deviceId)
            }
            Basket.find { Baskets.user eq userId }
                .sortedByDescending { it.creationTime }
                .sumOf { product -> product.amount.int.toBigDecimal() * product.device.price.bigDecimal }
                .let(::Price).right()
        }

    override suspend fun removeFromBasket(userId: UserId, deviceId: DeviceId): Either<AbstractBackendException, Price> =
        newSuspendedTransaction(Dispatchers.IO) {
            find(userId, deviceId)?.delete()
            Basket.find { Baskets.user eq userId }
                .sortedByDescending { it.creationTime }
                .sumOf { product -> product.amount.int.toBigDecimal() * product.device.price.bigDecimal }
                .let(::Price).right()
        }

    override suspend fun getAmount(userId: UserId, deviceId: DeviceId): Either<AbstractBackendException, Amount> =
        newSuspendedTransaction(Dispatchers.IO) {
            (find(userId, deviceId)?.amount ?: Amount(0)).right()
        }

    override suspend fun clearBasket(userId: UserId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Basket.find { Baskets.user eq userId }
                .parTraverse {
                    it.delete()
                }
            true.right()
        }

    override suspend fun get(id: BasketId): Either<AbstractBackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            Basket.findById(id)?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.basketByIdNotFoundException(
                id
            ).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<BasketModel>> =
        RepositoryUtil.generalGetAll(Baskets, pagination, resultRowMapper)

    override suspend fun post(createModel: BasketCreateModel): Either<AbstractBackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction userByIdNotFound(createModel.userId).left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound(
                createModel.deviceId
            ).left()
            Basket.new {
                this.user = user
                this.device = device
                this.amount = createModel.amount
            }.let(mapper::invoke)
        }

    override suspend fun post(id: BasketId, createModel: BasketCreateModel): Either<AbstractBackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val user = User.findById(createModel.userId) ?: return@newSuspendedTransaction userByIdNotFound(createModel.userId).left()
            val device = Device.findById(createModel.deviceId) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound(
                createModel.deviceId
            ).left()
            Basket.findById(id)?.apply {
                this.user = user
                this.device = device
                this.amount = createModel.amount
                this.updateTime = UpdateTime.now()
            }?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.basketByIdNotFoundException(id).left()
        }

    override suspend fun put(id: BasketId, updateModel: BasketUpdateModel): Either<AbstractBackendException, BasketModel> =
        newSuspendedTransaction(Dispatchers.IO) {

            val user = updateModel.userId?.let {
                User.findById(it) ?: return@newSuspendedTransaction userByIdNotFound(it).left()
            }
            val device = updateModel.deviceId?.let {
                Device.findById(it) ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.deviceByIdNotFound(
                    it
                ).left()
            }
            Basket.findById(id)?.apply {
                user?.let { this.user = it }
                device?.let { this.device = it }
                updateModel.amount?.let { this.amount = it }
                this.updateTime = UpdateTime.now()
            }?.let(mapper::invoke) ?: ru.novolmob.exposedbackendapi.exceptions.basketByIdNotFoundException(id).left()
        }

    override suspend fun delete(id: BasketId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            Basket.findById(id)?.run {
                delete()
                true.right()
            } ?: false.right()
        }
}