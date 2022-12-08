package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.OrderStatuses
import ru.novolmob.database.tables.Orders
import java.util.*

class OrderStatus(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<OrderStatus>(OrderStatuses)

    var order by Order referencedOn OrderStatuses.order
    var worker by Worker referencedOn OrderStatuses.worker
    var creationDate by Orders.creationDate
}