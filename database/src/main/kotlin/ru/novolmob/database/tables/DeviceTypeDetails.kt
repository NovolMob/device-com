package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.updateDate

object DeviceTypeDetails: UUIDTable() {
    val deviceType = reference("device_type_id", DeviceTypes)
    val title = text("title")
    val description = text("description")
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}