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
import ru.novolmob.backendapi.exceptions.basketByIdNotFoundException
import ru.novolmob.backendapi.exceptions.deviceByIdNotFound
import ru.novolmob.backendapi.exceptions.userByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IBasketRepository
import ru.novolmob.backendapi.repositories.IDeviceDetailRepository
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.Price.Companion.sumOf
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.BasketId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.exposeddatabase.entities.Basket
import ru.novolmob.exposeddatabase.entities.Device
import ru.novolmob.exposeddatabase.entities.User
import ru.novolmob.exposeddatabase.tables.Baskets

class BasketRepositoryImpl(
    mapper: Mapper<Basket, BasketModel>,
    resultRowMapper: Mapper<ResultRow, BasketModel>,
    val deviceDetailRepository: IDeviceDetailRepository,
): IBasketRepository, AbstractCrudRepository<BasketId, Basket.Companion, Basket, BasketModel, BasketCreateModel, BasketUpdateModel>(
    Basket.Companion, mapper, resultRowMapper, ::basketByIdNotFoundException
) {

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
                                    id = it.parentId,
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
                            totalPrice = list.sumOf { product -> product.device.price * product.amount.int }
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
                .sumOf { product -> product.device.price * product.amount.int }.right()
        }

    override suspend fun removeFromBasket(userId: UserId, deviceId: DeviceId): Either<AbstractBackendException, Price> =
        newSuspendedTransaction(Dispatchers.IO) {
            find(userId, deviceId)?.delete()
            Basket.find { Baskets.user eq userId }
                .sortedByDescending { it.creationTime }
                .sumOf { product -> product.device.price * product.amount.int }.right()
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

    override fun Basket.Companion.new(createModel: BasketCreateModel): Either<AbstractBackendException, Basket> {
        val user = User.findById(createModel.userId) ?: return userByIdNotFound(createModel.userId).left()
        val device = Device.findById(createModel.deviceId) ?: return deviceByIdNotFound(createModel.deviceId).left()
        return new {
            this.user = user
            this.device = device
            this.amount = createModel.amount
        }.right()
    }

    override fun Basket.applyC(createModel: BasketCreateModel): Either<AbstractBackendException, Basket> {
        val user = User.findById(createModel.userId) ?: return userByIdNotFound(createModel.userId).left()
        val device = Device.findById(createModel.deviceId) ?: return deviceByIdNotFound(createModel.deviceId).left()
        return apply {
            this.user = user
            this.device = device
            this.amount = createModel.amount
            this.updateTime = UpdateTime.now()
        }.right()
    }

    override fun Basket.applyU(updateModel: BasketUpdateModel): Either<AbstractBackendException, Basket> {
        val user = updateModel.userId?.let {
            User.findById(it) ?: return userByIdNotFound(it).left()
        }
        val device = updateModel.deviceId?.let {
            Device.findById(it) ?: return deviceByIdNotFound(it).left()
        }
        return apply {
            user?.let { this.user = it }
            device?.let { this.device = it }
            updateModel.amount?.let { this.amount = it }
            this.updateTime = UpdateTime.now()
        }.right()
    }

}