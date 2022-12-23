package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.exposeddatabase.tables.DeviceDetails

class DeviceDetail(id: EntityID<DeviceDetailId>) : Entity<DeviceDetailId>(id) {
    companion object: EntityClass<DeviceDetailId, DeviceDetail>(DeviceDetails)

    var device by Device referencedOn DeviceDetails.device
    var title by DeviceDetails.title
    var description by DeviceDetails.description
    var features by DeviceDetails.features
    var language by DeviceDetails.language
    var updateDate by DeviceDetails.updateDate
    var creationDate by DeviceDetails.creationDate
}