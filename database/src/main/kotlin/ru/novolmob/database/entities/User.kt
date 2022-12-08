package ru.novolmob.database.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.database.tables.Users
import java.util.*

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<User>(Users)

    var firstname by Users.firstname
    var lastname by Users.lastname
    var patronymic by Users.patronymic
    var birthday by Users.birthday
    var city by Users.city
    var language by Users.language
    var updateDate by Users.updateDate
    var creationDate by Users.creationDate
}