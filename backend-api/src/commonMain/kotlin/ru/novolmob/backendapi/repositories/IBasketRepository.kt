package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.BasketFullModel
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.UserId

interface IBasketRepository: IRepository {
    suspend fun getBasket(userId: UserId, language: Language): Either<AbstractBackendException, BasketFullModel>
    suspend fun setInBasket(userId: UserId, deviceId: DeviceId, amount: Amount): Either<AbstractBackendException, Price>
    suspend fun removeFromBasket(userId: UserId, deviceId: DeviceId): Either<AbstractBackendException, Price>
    suspend fun getAmount(userId: UserId, deviceId: DeviceId): Either<AbstractBackendException, Amount>
    suspend fun clearBasket(userId: UserId): Either<AbstractBackendException, Boolean>
}