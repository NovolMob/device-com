package ru.novolmob.exposeddatabase.entities.details

import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.exposeddatabase.entities.Point
import ru.novolmob.exposeddatabase.tables.details.PointDetails

class PointDetail(id: EntityID<PointDetailId>) : DetailEntity<PointDetailId, Point, PointId>(id) {
    companion object: DetailEntityClass<PointDetailId, PointDetail, Point, PointId>(PointDetails)

    override var parent by Point referencedOn PointDetails.parent
    var address by PointDetails.address
    var schedule by PointDetails.schedule
    var description by PointDetails.description
    override var language by PointDetails.language
    var updateDate by PointDetails.updateDate
    var creationDate by PointDetails.creationDate
}