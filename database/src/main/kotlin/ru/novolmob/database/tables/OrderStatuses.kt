package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate

object OrderStatuses: UUIDTable() {
    val order = reference("order_id", Orders)
    val worker = reference("worker_id", Workers)
    val creationDate = creationDate()
}