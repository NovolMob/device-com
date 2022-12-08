package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.PointDetails
import java.util.*

class PointDetail(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<PointDetail>(PointDetails)

    var point by Point referencedOn PointDetails.point
    var address by PointDetails.address
    var schedule by PointDetails.schedule
    var description by PointDetails.description
    var language by PointDetails.language
    var updateDate by PointDetails.updateDate
    var creationDate by PointDetails.creationDate
}