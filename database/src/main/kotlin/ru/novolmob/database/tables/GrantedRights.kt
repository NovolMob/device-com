package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate

object GrantedRights: UUIDTable() {
    val worker = reference("worker_id", Workers)
    val title = text("title")
    val admin = reference("admin_id", Workers)
    val creationDate = creationDate()
}