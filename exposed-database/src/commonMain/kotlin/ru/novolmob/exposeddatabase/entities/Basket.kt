package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.BasketId
import ru.novolmob.exposeddatabase.tables.Baskets

class Basket(id: EntityID<BasketId>) : Entity<BasketId>(id) {
    companion object: EntityClass<BasketId, Basket>(Baskets)

    var user by User referencedOn Baskets.user
    var device by Device referencedOn Baskets.device
    var amount by Baskets.amount
    var updateTime by Baskets.updateTime
    var creationTime by Baskets.creationTime
}