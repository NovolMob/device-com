package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.exposeddatabase.models.ids.DeviceTypeId
import ru.novolmob.exposeddatabase.tables.DeviceTypes

class DeviceType(id: EntityID<DeviceTypeId>) : Entity<DeviceTypeId>(id) {
    companion object: EntityClass<DeviceTypeId, DeviceType>(DeviceTypes)

    var updateDate by DeviceTypes.updateDate
    var creationDate by DeviceTypes.creationDate
}