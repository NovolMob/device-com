package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.exposeddatabase.models.ids.PointDetailId
import ru.novolmob.exposeddatabase.tables.PointDetails

class PointDetail(id: EntityID<PointDetailId>) : Entity<PointDetailId>(id) {
    companion object: EntityClass<PointDetailId, PointDetail>(PointDetails)

    var point by Point referencedOn PointDetails.point
    var address by PointDetails.address
    var schedule by PointDetails.schedule
    var description by PointDetails.description
    var language by PointDetails.language
    var updateDate by PointDetails.updateDate
    var creationDate by PointDetails.creationDate
}