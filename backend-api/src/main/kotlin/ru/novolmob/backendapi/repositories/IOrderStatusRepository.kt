package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.OrderStatusCreateModel
import ru.novolmob.backendapi.models.OrderStatusModel
import ru.novolmob.backendapi.models.OrderStatusUpdateModel
import ru.novolmob.database.models.ids.OrderStatusId

interface IOrderStatusRepository: ICrudRepository<OrderStatusId, OrderStatusModel, OrderStatusCreateModel, OrderStatusUpdateModel>