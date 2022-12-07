package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.updateDate

object OrderStatusDetails: UUIDTable() {
    val orderStatus = reference("order_status", OrderStatuses)
    val title = text("title")
    val description = text("description")
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}