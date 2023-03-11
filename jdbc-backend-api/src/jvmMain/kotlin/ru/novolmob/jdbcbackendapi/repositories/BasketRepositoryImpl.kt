package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.BasketFullModel
import ru.novolmob.backendapi.models.BasketItemModel
import ru.novolmob.backendapi.models.BasketModel
import ru.novolmob.backendapi.repositories.IBasketRepository
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.list
import ru.novolmob.jdbcdatabase.functions.GettingBasketFunction
import ru.novolmob.jdbcdatabase.procedures.RemoveFromBasketProcedure
import ru.novolmob.jdbcdatabase.procedures.SetAmountInBasketProcedure
import ru.novolmob.jdbcdatabase.procedures.TotalCostProcedure
import ru.novolmob.jdbcdatabase.tables.Baskets
import java.sql.ResultSet

class BasketRepositoryImpl(
    val mapper: Mapper<ResultSet, BasketModel>,
    val itemMapper: Mapper<ResultSet, BasketItemModel>
): IBasketRepository {
    override suspend fun getBasket(
        userId: UserId,
        language: Language
    ): Either<AbstractBackendException, BasketFullModel> =
        GettingBasketFunction.call(userId, language) { list(itemMapper) }
            .flatMap { list ->
                Either.backend {
                    "dasdasdas".toBigDecimalOrNull()
                    BasketFullModel(list = list, totalPrice = TotalCostProcedure.call(userId))
                }
            }

    override suspend fun setInBasket(
        userId: UserId,
        deviceId: DeviceId,
        amount: Amount
    ): Either<AbstractBackendException, Price> =
        SetAmountInBasketProcedure.call(userId, deviceId, amount).right()

    override suspend fun removeFromBasket(userId: UserId, deviceId: DeviceId): Either<AbstractBackendException, Price> =
        RemoveFromBasketProcedure.call(userId, deviceId).right()

    override suspend fun getAmount(userId: UserId, deviceId: DeviceId): Either<AbstractBackendException, Amount> =
        Baskets.getAmount(userId, deviceId).right()

    override suspend fun clearBasket(userId: UserId): Either<AbstractBackendException, Boolean> =
        (Baskets.deleteFor(userId) > 0).right()
}