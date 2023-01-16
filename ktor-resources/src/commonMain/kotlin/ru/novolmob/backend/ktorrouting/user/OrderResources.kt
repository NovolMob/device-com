package ru.novolmob.backend.ktorrouting.user

import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId


expect class Orders {
    class Confirm(pointId: PointId) {
        val pointId: PointId
    }
    class Id(id: OrderId) {
        val id: OrderId
    }
}