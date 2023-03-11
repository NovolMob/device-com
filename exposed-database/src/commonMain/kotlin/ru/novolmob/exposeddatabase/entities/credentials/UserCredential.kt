package ru.novolmob.exposeddatabase.entities.credentials

import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.exposeddatabase.entities.User
import ru.novolmob.exposeddatabase.tables.credentials.UserCredentials

class UserCredential(id: EntityID<CredentialId>) : CredentialEntity<User, UserId>(id) {
    companion object: CredentialEntityClass<UserCredential, User, UserId>(UserCredentials)

    var phoneNumber by UserCredentials.phoneNumber
    var email by UserCredentials.email
    var password by UserCredentials.password
    var updateDate by UserCredentials.updateDate
    override var parent by User referencedOn UserCredentials.parent
}