package ru.novolmob.database.tables.credentials

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.novolmob.database.extensions.TableExtension.phoneNumber
import ru.novolmob.database.extensions.TableExtension.updateDate

open class Credentials: UUIDTable() {
    val phoneNumber = phoneNumber("phone_number")
    val email = text("email").nullable()
    val password = text("password")
    val updateDate = updateDate()
}