package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId

@Serializable
@Resource("orders")
class Orders() {
    @Serializable
    @Resource("confirm")
    class Confirm(val orders: Orders, val pointId: PointId)
    @Serializable
    @Resource("{id}")
    class Id(val orders: Orders, val id: OrderId)
}