package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.Workers
import java.util.*

class Worker(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<Worker>(Workers)

    var point by Point referencedOn Workers.point
    var firstname by Workers.firstname
    var lastname by Workers.lastname
    var patronymic by Workers.patronymic
    var birthday by Workers.birthday
    var language by Workers.language
    var updateDate by Workers.updateDate
    var creationDate by Workers.creationDate
}