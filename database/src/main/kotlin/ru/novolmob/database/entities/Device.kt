package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.Devices
import java.util.*

class Device(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<Device>(Devices)

    var article by Devices.article
    var type by DeviceType referencedOn Devices.type
    var price by Devices.price
    var updateDate by Devices.updateDate
    var creationDate by Devices.creationDate
}