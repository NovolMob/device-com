package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.Orders
import java.util.*

class Order(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<Order>(Orders)

    var user by User referencedOn Orders.user
    var point by Point referencedOn Orders.point
    var totalCost by Orders.totalCost
    var creationDate by Orders.creationDate
}