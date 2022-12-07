package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.updateDate

object Basket: UUIDTable() {
    val user = reference("user_id", Users)
    val device = reference("device_id", Devices)
    val amount = integer("amount")
    val updateDate = updateDate()
    val creationDate = creationDate()
}