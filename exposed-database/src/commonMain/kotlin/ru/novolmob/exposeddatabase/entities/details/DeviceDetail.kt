package ru.novolmob.exposeddatabase.entities.details

import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.exposeddatabase.entities.Device
import ru.novolmob.exposeddatabase.tables.details.DeviceDetails

class DeviceDetail(id: EntityID<DeviceDetailId>) : DetailEntity<DeviceDetailId, Device, DeviceId>(id) {
    companion object: DetailEntityClass<DeviceDetailId, DeviceDetail, Device, DeviceId>(DeviceDetails)

    override var parent by Device referencedOn DeviceDetails.parent
    var title by DeviceDetails.title
    var description by DeviceDetails.description
    var features by DeviceDetails.features
    override var language by DeviceDetails.language
    var updateDate by DeviceDetails.updateDate
    var creationDate by DeviceDetails.creationDate
}