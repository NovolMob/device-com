package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.exposeddatabase.tables.credentials.UserCredentials

class UserCredential(id: EntityID<CredentialId>) : Entity<CredentialId>(id) {
    companion object: EntityClass<CredentialId, UserCredential>(UserCredentials)

    var phoneNumber by UserCredentials.phoneNumber
    var email by UserCredentials.email
    var password by UserCredentials.password
    var updateDate by UserCredentials.updateDate
    var user by User referencedOn UserCredentials.user
}