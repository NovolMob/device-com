package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.BasketModel
import ru.novolmob.exposeddatabase.entities.Basket

class BasketMapper: Mapper<Basket, BasketModel> {
    override fun invoke(input: Basket): Either<AbstractBackendException, BasketModel> =
        BasketModel(
            id = input.id.value,
            userId = input.user.id.value,
            deviceId = input.device.id.value,
            amount = input.amount
        ).right()
}