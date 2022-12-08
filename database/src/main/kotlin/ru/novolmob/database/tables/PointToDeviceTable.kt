package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.updateDate

object PointToDeviceTable: UUIDTable() {
    val point = reference("point", Points)
    val device = reference("device", Devices)
    val amount = integer("amount")
    val updateDate = updateDate()
    val creationDate = creationDate()
}