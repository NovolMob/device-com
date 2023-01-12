package ru.novolmob.exposeddatabase.tables.credentials

import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.exposeddatabase.tables.Workers

object WorkerCredentials: CredentialTable<WorkerId>() {
    override val parent = reference("worker_id", Workers, onDelete = ReferenceOption.CASCADE).uniqueIndex()
}