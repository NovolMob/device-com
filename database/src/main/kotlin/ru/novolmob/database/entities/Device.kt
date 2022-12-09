package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.tables.Devices

class Device(id: EntityID<DeviceId>) : Entity<DeviceId>(id) {
    companion object: EntityClass<DeviceId, Device>(Devices)

    var code by Devices.code
    var type by DeviceType referencedOn Devices.type
    var price by Devices.price
    var updateDate by Devices.updateDate
    var creationDate by Devices.creationDate
}