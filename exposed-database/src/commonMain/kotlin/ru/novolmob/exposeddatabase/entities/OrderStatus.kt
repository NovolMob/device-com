package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.exposeddatabase.entities.details.OrderStatusDetail
import ru.novolmob.exposeddatabase.tables.details.OrderStatusDetails
import ru.novolmob.exposeddatabase.tables.OrderStatuses

class OrderStatus(id: EntityID<OrderStatusId>) : Entity<OrderStatusId>(id) {
    companion object: EntityClass<OrderStatusId, OrderStatus>(OrderStatuses)

    var active by OrderStatuses.active
    var creationDate by OrderStatuses.creationDate

    val details by OrderStatusDetail referrersOn OrderStatusDetails.parent
}