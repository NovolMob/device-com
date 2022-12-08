package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.DeviceTypes
import java.util.*

class DeviceType(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<DeviceType>(DeviceTypes)

    var updateDate by DeviceTypes.updateDate
    var creationDate by DeviceTypes.creationDate
}