package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.OrderCreateModel
import ru.novolmob.backendapi.models.OrderModel
import ru.novolmob.backendapi.models.OrderUpdateModel
import ru.novolmob.database.models.ids.OrderId

interface IOrderRepository: ICrudRepository<OrderId, OrderModel, OrderCreateModel, OrderUpdateModel>