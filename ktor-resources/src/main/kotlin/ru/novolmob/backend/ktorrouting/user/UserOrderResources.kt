package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.database.models.ids.OrderId

@Serializable
@Resource("orders")
class Orders(val active: Boolean = false) {
    @Serializable
    @Resource("{id}")
    class Id(val orders: Orders, val id: OrderId)
}