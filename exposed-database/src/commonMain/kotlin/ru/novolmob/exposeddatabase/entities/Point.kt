package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.exposeddatabase.entities.details.PointDetail
import ru.novolmob.exposeddatabase.tables.details.PointDetails
import ru.novolmob.exposeddatabase.tables.Points

class Point(id: EntityID<PointId>) : Entity<PointId>(id) {
    companion object: EntityClass<PointId, Point>(Points)

    var city by City referencedOn Points.city
    var updateDate by Points.updateDate
    var creationDate by Points.creationDate

    val details by PointDetail referrersOn PointDetails.parent
}