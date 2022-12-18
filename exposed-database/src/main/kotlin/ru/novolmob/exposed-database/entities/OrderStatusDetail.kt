package ru.novolmob.`exposed-database`.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.`exposed-database`.models.ids.OrderStatusDetailId
import ru.novolmob.`exposed-database`.tables.OrderStatusDetails

class OrderStatusDetail(id: EntityID<OrderStatusDetailId>) : Entity<OrderStatusDetailId>(id) {
    companion object: EntityClass<OrderStatusDetailId, OrderStatusDetail>(OrderStatusDetails)

    var orderStatus by OrderStatus referencedOn OrderStatusDetails.orderStatus
    var title by OrderStatusDetails.title
    var description by OrderStatusDetails.description
    var language by OrderStatusDetails.language
    var updateDate by OrderStatusDetails.updateDate
    var creationDate by OrderStatusDetails.creationDate
}