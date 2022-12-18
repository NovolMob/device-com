package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.models.ids.OrderStatusId
import ru.novolmob.database.tables.OrderStatuses

class OrderStatus(id: EntityID<OrderStatusId>) : Entity<OrderStatusId>(id) {
    companion object: EntityClass<OrderStatusId, OrderStatus>(OrderStatuses)

    var active by OrderStatuses.active
    var creationDate by OrderStatuses.creationDate
}