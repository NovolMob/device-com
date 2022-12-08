package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.PointToDeviceTable
import java.util.*

class PointToDeviceEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<PointToDeviceEntity>(PointToDeviceTable)

    var point by Point referencedOn PointToDeviceTable.point
    var device by Device referencedOn PointToDeviceTable.device
    var amount by PointToDeviceTable.amount
    var updateDate by PointToDeviceTable.updateDate
    var creationDate by PointToDeviceTable.creationDate
}