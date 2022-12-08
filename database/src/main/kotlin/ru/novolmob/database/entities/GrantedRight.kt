package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.GrantedRights
import java.util.*

class GrantedRight(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<GrantedRight>(GrantedRights)

    var worker by Worker referencedOn GrantedRights.worker
    var title by GrantedRights.title
    var admin by Worker referencedOn GrantedRights.admin
    var creationDate by GrantedRights.creationDate
}