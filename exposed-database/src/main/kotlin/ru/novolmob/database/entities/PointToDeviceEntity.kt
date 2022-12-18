package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.models.ids.PointToDeviceEntityId
import ru.novolmob.database.tables.PointToDeviceTable

class PointToDeviceEntity(id: EntityID<PointToDeviceEntityId>) : Entity<PointToDeviceEntityId>(id) {
    companion object: EntityClass<PointToDeviceEntityId, PointToDeviceEntity>(PointToDeviceTable)

    var point by Point referencedOn PointToDeviceTable.point
    var device by Device referencedOn PointToDeviceTable.device
    var amount by PointToDeviceTable.amount
    var updateDate by PointToDeviceTable.updateDate
    var creationDate by PointToDeviceTable.creationDate
}