package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.exposeddatabase.tables.DeviceTypeDetails

class DeviceTypeDetail(id: EntityID<DeviceTypeDetailId>) : Entity<DeviceTypeDetailId>(id) {
    companion object: EntityClass<DeviceTypeDetailId, DeviceTypeDetail>(DeviceTypeDetails)

    var deviceType by DeviceType referencedOn DeviceTypeDetails.deviceType
    var title by DeviceTypeDetails.title
    var description by DeviceTypeDetails.description
    var language by DeviceTypeDetails.language
    var updateDate by DeviceTypeDetails.updateDate
    var creationDate by DeviceTypeDetails.creationDate
}