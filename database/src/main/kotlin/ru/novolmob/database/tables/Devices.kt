package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.updateDate

object Devices: UUIDTable() {
    val article = text("article")
    val type = reference("type_id", DeviceTypes)
    val price = decimal("price", 10, 2)
    val updateDate = updateDate()
    val creationDate = creationDate()
}