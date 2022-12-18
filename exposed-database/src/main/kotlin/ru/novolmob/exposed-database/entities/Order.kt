package ru.novolmob.`exposed-database`.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.`exposed-database`.models.ids.OrderId
import ru.novolmob.`exposed-database`.tables.Orders

class Order(id: EntityID<OrderId>) : Entity<OrderId>(id) {
    companion object: EntityClass<OrderId, Order>(Orders)

    var user by User referencedOn Orders.user
    var point by Point referencedOn Orders.point
    var totalCost by Orders.totalCost
    var creationDate by Orders.creationDate
}