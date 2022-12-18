package ru.novolmob.`exposed-database`.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.`exposed-database`.models.ids.OrderToDeviceEntityId
import ru.novolmob.`exposed-database`.tables.OrderToDeviceTable

class OrderToDeviceEntity(id: EntityID<OrderToDeviceEntityId>) : Entity<OrderToDeviceEntityId>(id) {
    companion object: EntityClass<OrderToDeviceEntityId, OrderToDeviceEntity>(OrderToDeviceTable)

    var order by Order referencedOn OrderToDeviceTable.order
    var device by Device referencedOn OrderToDeviceTable.device
    var amount by OrderToDeviceTable.amount
    var priceForOne by OrderToDeviceTable.priceForOne
}