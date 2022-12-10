package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.BasketCreateModel
import ru.novolmob.backendapi.models.BasketModel
import ru.novolmob.backendapi.models.BasketUpdateModel
import ru.novolmob.database.models.ids.BasketId

interface IBasketRepository: ICrudRepository<BasketId, BasketModel, BasketCreateModel, BasketUpdateModel>