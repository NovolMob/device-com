package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.OrderToDeviceTable
import java.util.*

class OrderToDeviceEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<OrderToDeviceEntity>(OrderToDeviceTable)

    var order by Order referencedOn OrderToDeviceTable.order
    var device by Device referencedOn OrderToDeviceTable.device
    var amount by OrderToDeviceTable.amount
    var priceForOne by OrderToDeviceTable.priceForOne
}