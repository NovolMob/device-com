package ru.novolmob.exposeddatabase.entities.credentials

import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.exposeddatabase.entities.Worker
import ru.novolmob.exposeddatabase.tables.credentials.WorkerCredentials

class WorkerCredential(id: EntityID<CredentialId>) : CredentialEntity<Worker, WorkerId>(id) {
    companion object: CredentialEntityClass<WorkerCredential, Worker, WorkerId>(WorkerCredentials)

    var phoneNumber by WorkerCredentials.phoneNumber
    var email by WorkerCredentials.email
    var password by WorkerCredentials.password
    var updateDate by WorkerCredentials.updateDate
    override var parent by Worker referencedOn WorkerCredentials.parent
}