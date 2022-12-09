package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.models.ids.WorkerId
import ru.novolmob.database.tables.Workers

class Worker(id: EntityID<WorkerId>) : Entity<WorkerId>(id) {
    companion object: EntityClass<WorkerId, Worker>(Workers)

    var point by Point optionalReferencedOn Workers.point
    var firstname by Workers.firstname
    var lastname by Workers.lastname
    var patronymic by Workers.patronymic
    var language by Workers.language
    var updateDate by Workers.updateDate
    var creationDate by Workers.creationDate
}