package ru.novolmob.exposeddatabase.tables.credentials

import ru.novolmob.exposeddatabase.tables.Workers

object WorkerCredentials: Credentials() {
    val worker = reference("worker_id", Workers).uniqueIndex()
}