package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.OrderToStatusEntityId
import ru.novolmob.exposeddatabase.tables.OrderToStatusTable

class OrderToStatusEntity(id: EntityID<OrderToStatusEntityId>) : Entity<OrderToStatusEntityId>(id) {
    companion object: EntityClass<OrderToStatusEntityId, OrderToStatusEntity>(OrderToStatusTable)

    var order by Order referencedOn OrderToStatusTable.order
    var status by OrderStatus referencedOn OrderToStatusTable.orderStatus
    var worker by Worker referencedOn OrderToStatusTable.worker
    var creationDate by OrderToStatusTable.creationDate
}