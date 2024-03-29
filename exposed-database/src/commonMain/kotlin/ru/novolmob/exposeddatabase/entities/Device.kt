package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.exposeddatabase.entities.details.DeviceDetail
import ru.novolmob.exposeddatabase.tables.details.DeviceDetails
import ru.novolmob.exposeddatabase.tables.Devices

class Device(id: EntityID<DeviceId>) : Entity<DeviceId>(id) {
    companion object: EntityClass<DeviceId, Device>(Devices)

    var article by Devices.code
    var type by DeviceType referencedOn Devices.type
    var amount by Devices.amount
    var price by Devices.price
    var updateDate by Devices.updateDate
    var creationDate by Devices.creationDate

    val details by DeviceDetail referrersOn DeviceDetails.parent
}