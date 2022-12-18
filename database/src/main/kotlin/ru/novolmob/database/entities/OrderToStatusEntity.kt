package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.models.ids.OrderToStatusEntityId
import ru.novolmob.database.tables.OrderToStatusTable

class OrderToStatusEntity(id: EntityID<OrderToStatusEntityId>) : Entity<OrderToStatusEntityId>(id) {
    companion object: EntityClass<OrderToStatusEntityId, OrderToStatusEntity>(OrderToStatusTable)

    val order by Order referencedOn OrderToStatusTable.order
    val status by OrderStatus referencedOn OrderToStatusTable.orderStatus
    val worker by Worker referencedOn OrderToStatusTable.worker
    val creationDate by OrderToStatusTable.creationDate
}