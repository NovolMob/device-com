package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.BasketCreateModel
import ru.novolmob.backendapi.models.BasketItemModel
import ru.novolmob.backendapi.models.BasketModel
import ru.novolmob.backendapi.models.BasketUpdateModel
import ru.novolmob.database.models.Amount
import ru.novolmob.database.models.ids.BasketId
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.UserId

interface IBasketRepository: ICrudRepository<BasketId, BasketModel, BasketCreateModel, BasketUpdateModel> {
    suspend fun getBasket(userId: UserId): Either<BackendException, List<BasketItemModel>>
    suspend fun setInBasket(userId: UserId, deviceId: DeviceId, amount: Amount): Either<BackendException, Boolean>
    suspend fun removeFromBasket(userId: UserId, deviceId: DeviceId): Either<BackendException, Boolean>
    suspend fun getAmount(userId: UserId, deviceId: DeviceId): Either<BackendException, Amount>
}