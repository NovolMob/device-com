package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.DeviceTypeDetails
import java.util.*

class DeviceTypeDetail(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<DeviceTypeDetail>(DeviceTypeDetails)

    var deviceType by DeviceType referencedOn DeviceTypeDetails.deviceType
    var title by DeviceTypeDetails.title
    var description by DeviceTypeDetails.description
    var language by DeviceTypeDetails.language
    var updateDate by DeviceTypeDetails.updateDate
    var creationDate by DeviceTypeDetails.creationDate
}