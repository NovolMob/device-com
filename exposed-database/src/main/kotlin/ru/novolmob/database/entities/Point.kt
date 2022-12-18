package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.models.ids.PointId
import ru.novolmob.database.tables.Points

class Point(id: EntityID<PointId>) : Entity<PointId>(id) {
    companion object: EntityClass<PointId, Point>(Points)

    var updateDate by Points.updateDate
    var creationDate by Points.creationDate
}