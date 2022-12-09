package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.models.ids.GrantedRightId
import ru.novolmob.database.tables.GrantedRights

class GrantedRight(id: EntityID<GrantedRightId>) : Entity<GrantedRightId>(id) {
    companion object: EntityClass<GrantedRightId, GrantedRight>(GrantedRights)

    var worker by Worker referencedOn GrantedRights.worker
    var code by GrantedRights.code
    var admin by Worker referencedOn GrantedRights.admin
    var creationDate by GrantedRights.creationDate
}