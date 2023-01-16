package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId

@Serializable
@Resource("orders")
actual class Orders {
    @Serializable
    @Resource("confirm")
    actual class Confirm actual constructor(actual val pointId: PointId)
    @Serializable
    @Resource("{id}")
    actual class Id actual constructor(actual val id: OrderId)
}