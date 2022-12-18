package ru.novolmob.`exposed-database`.tables.credentials

import ru.novolmob.`exposed-database`.tables.Workers

object WorkerCredentials: Credentials() {
    val worker = reference("worker_id", Workers).uniqueIndex()
}