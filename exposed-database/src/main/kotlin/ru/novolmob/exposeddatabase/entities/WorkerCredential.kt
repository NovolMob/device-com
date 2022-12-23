package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.exposeddatabase.tables.credentials.WorkerCredentials

class WorkerCredential(id: EntityID<CredentialId>) : Entity<CredentialId>(id) {
    companion object: EntityClass<CredentialId, WorkerCredential>(WorkerCredentials)

    var phoneNumber by WorkerCredentials.phoneNumber
    var email by WorkerCredentials.email
    var password by WorkerCredentials.password
    var updateDate by WorkerCredentials.updateDate
    var worker by Worker referencedOn WorkerCredentials.worker
}