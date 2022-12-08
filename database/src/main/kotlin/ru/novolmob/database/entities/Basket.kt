package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.Baskets
import java.util.*

class Basket(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<Basket>(Baskets)

    var user by User referencedOn Baskets.user
    var device by Device referencedOn Baskets.device
    var amount by Baskets.amount
    var updateDate by Baskets.updateDate
    var creationDate by Baskets.creationDate
}