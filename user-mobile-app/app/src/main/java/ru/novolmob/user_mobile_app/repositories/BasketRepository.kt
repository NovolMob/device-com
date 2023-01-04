package ru.novolmob.user_mobile_app.repositories

import arrow.core.Either
import io.ktor.client.*
import ru.novolmob.backend.ktorrouting.user.Basket
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.BasketFullModel
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.user_mobile_app.utils.KtorUtil.delete
import ru.novolmob.user_mobile_app.utils.KtorUtil.get
import ru.novolmob.user_mobile_app.utils.KtorUtil.post

interface IBasketRepository: IRepository {
    suspend fun getBasket(): Either<BackendException, BasketFullModel>
    suspend fun setInBasket(deviceId: DeviceId, amount: Amount): Either<BackendException, Price>
    suspend fun removeFromBasket(deviceId: DeviceId): Either<BackendException, Price>
    suspend fun getAmountInBasket(deviceId: DeviceId): Either<BackendException, Amount>
}

class BasketRepositoryImpl(
    private val client: HttpClient
): IBasketRepository {
    override suspend fun getBasket(): Either<BackendException, BasketFullModel> =
        client.get(Basket())

    override suspend fun setInBasket(
        deviceId: DeviceId,
        amount: Amount
    ): Either<BackendException, Price> =
        client.post(Basket.Device(basket = Basket(), id = deviceId), body = amount)

    override suspend fun removeFromBasket(deviceId: DeviceId): Either<BackendException, Price> =
        client.delete(Basket.Device(basket = Basket(), id = deviceId))

    override suspend fun getAmountInBasket(deviceId: DeviceId): Either<BackendException, Amount> =
        client.get(Basket.Device(basket = Basket(), id = deviceId))
}