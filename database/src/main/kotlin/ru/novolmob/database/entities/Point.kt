package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.Points
import java.util.*

class Point(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<Point>(Points)


    var updateDate by Points.updateDate
    var creationDate by Points.creationDate
}