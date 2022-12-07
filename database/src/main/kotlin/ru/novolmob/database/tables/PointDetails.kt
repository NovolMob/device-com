package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.map
import ru.novolmob.database.extensions.TableExtension.updateDate

object PointDetails: UUIDTable() {
    val point = reference("point_id", Points)
    val address = text("address")
    val schedule = map("schedule")
    val description = text("description")
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}