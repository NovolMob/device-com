package ru.novolmob.database.tables.credentials

import ru.novolmob.database.tables.Workers

object WorkerCredentials: Credentials() {
    val worker = reference("worker_id", Workers)
}