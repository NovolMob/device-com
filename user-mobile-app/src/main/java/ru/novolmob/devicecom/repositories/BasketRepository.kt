package ru.novolmob.devicecom.repositories

import arrow.core.Either
import io.ktor.client.*
import ru.novolmob.backend.ktorrouting.user.Basket
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.BasketFullModel
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.devicecom.utils.KtorUtil.delete
import ru.novolmob.devicecom.utils.KtorUtil.get
import ru.novolmob.devicecom.utils.KtorUtil.post

interface IBasketRepository: IRepository {
    suspend fun getBasket(): Either<AbstractBackendException, BasketFullModel>
    suspend fun setInBasket(deviceId: DeviceId, amount: Amount): Either<AbstractBackendException, Price>
    suspend fun removeFromBasket(deviceId: DeviceId): Either<AbstractBackendException, Price>
    suspend fun getAmountInBasket(deviceId: DeviceId): Either<AbstractBackendException, Amount>
}

class BasketRepositoryImpl(
    private val client: HttpClient
): IBasketRepository {
    override suspend fun getBasket(): Either<AbstractBackendException, BasketFullModel> =
        client.get(Basket())

    override suspend fun setInBasket(
        deviceId: DeviceId,
        amount: Amount
    ): Either<AbstractBackendException, Price> =
        client.post(Basket.Device(basket = Basket(), id = deviceId), body = amount)

    override suspend fun removeFromBasket(deviceId: DeviceId): Either<AbstractBackendException, Price> =
        client.delete(Basket.Device(basket = Basket(), id = deviceId))

    override suspend fun getAmountInBasket(deviceId: DeviceId): Either<AbstractBackendException, Amount> =
        client.get(Basket.Device(basket = Basket(), id = deviceId))
}