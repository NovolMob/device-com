package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.OrderStatusDetails
import ru.novolmob.database.tables.Workers
import java.util.*

class OrderStatusDetail(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<OrderStatusDetail>(OrderStatusDetails)

    var orderStatus by OrderStatus referencedOn OrderStatusDetails.orderStatus
    var title by OrderStatusDetails.title
    var description by OrderStatusDetails.description
    var language by Workers.language
    var updateDate by Workers.updateDate
    var creationDate by Workers.creationDate
}