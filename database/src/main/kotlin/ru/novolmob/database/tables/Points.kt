package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.updateDate

object Points: UUIDTable() {
    val updateDate = updateDate()
    val creationDate = creationDate()
}