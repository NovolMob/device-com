package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate

object Orders: UUIDTable() {
    val user = reference("user_id", Users)
    val point = reference("point_id", Points)
    val totalCost = decimal("total_cost", 10, 2)
    val creationDate = creationDate()
}