package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.BasketId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.UserId

interface IBasketRepository: ICrudRepository<BasketId, BasketModel, BasketCreateModel, BasketUpdateModel> {
    suspend fun getBasket(userId: UserId, language: Language): Either<BackendException, BasketFullModel>
    suspend fun setInBasket(userId: UserId, deviceId: DeviceId, amount: Amount): Either<BackendException, Price>
    suspend fun removeFromBasket(userId: UserId, deviceId: DeviceId): Either<BackendException, Price>
    suspend fun getAmount(userId: UserId, deviceId: DeviceId): Either<BackendException, Amount>
}