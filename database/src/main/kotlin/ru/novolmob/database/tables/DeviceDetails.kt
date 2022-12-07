package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.map
import ru.novolmob.database.extensions.TableExtension.updateDate

object DeviceDetails: UUIDTable() {
    val device = reference("device_id", Devices)
    val title = text("title")
    val description = text("description")
    val features = map("features")
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}