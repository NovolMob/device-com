package ru.novolmob.exposeddatabase.entities.details

import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.exposeddatabase.entities.DeviceType
import ru.novolmob.exposeddatabase.tables.details.DeviceTypeDetails

class DeviceTypeDetail(id: EntityID<DeviceTypeDetailId>) : DetailEntity<DeviceTypeDetailId, DeviceType, DeviceTypeId>(id) {
    companion object: DetailEntityClass<DeviceTypeDetailId, DeviceTypeDetail, DeviceType, DeviceTypeId>(DeviceTypeDetails)

    override var parent by DeviceType referencedOn DeviceTypeDetails.parent
    var title by DeviceTypeDetails.title
    var description by DeviceTypeDetails.description
    override var language by DeviceTypeDetails.language
    var updateDate by DeviceTypeDetails.updateDate
    var creationDate by DeviceTypeDetails.creationDate
}