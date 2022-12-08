package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.DeviceDetails
import java.util.*

class DeviceDetail(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<DeviceDetail>(DeviceDetails)

    var device by Device referencedOn DeviceDetails.device
    var title by DeviceDetails.title
    var description by DeviceDetails.description
    var features by DeviceDetails.features
    var language by DeviceDetails.language
    var updateDate by DeviceDetails.updateDate
    var creationDate by DeviceDetails.creationDate
}