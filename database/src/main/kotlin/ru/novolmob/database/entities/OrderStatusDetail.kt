package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.models.ids.OrderStatusDetailId
import ru.novolmob.database.tables.OrderStatusDetails
import ru.novolmob.database.tables.Workers

class OrderStatusDetail(id: EntityID<OrderStatusDetailId>) : Entity<OrderStatusDetailId>(id) {
    companion object: EntityClass<OrderStatusDetailId, OrderStatusDetail>(OrderStatusDetails)

    var orderStatus by OrderStatus referencedOn OrderStatusDetails.orderStatus
    var title by OrderStatusDetails.title
    var description by OrderStatusDetails.description
    var language by Workers.language
    var updateDate by Workers.updateDate
    var creationDate by Workers.creationDate
}