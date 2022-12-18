package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.exposeddatabase.models.ids.UserId
import ru.novolmob.exposeddatabase.tables.Users

class User(id: EntityID<UserId>) : Entity<UserId>(id) {
    companion object: EntityClass<UserId, User>(Users)

    var firstname by Users.firstname
    var lastname by Users.lastname
    var patronymic by Users.patronymic
    var birthday by Users.birthday
    var city by Users.city
    var language by Users.language
    var updateDate by Users.updateDate
    var creationDate by Users.creationDate
}