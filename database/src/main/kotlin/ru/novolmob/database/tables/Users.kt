package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.date
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.updateDate

object Users: UUIDTable() {
    val firstname = text("firstname")
    val lastname = text("lastname")
    val patronymic = text("patronymic").nullable()
    val birthday = date("birthday").nullable()
    val city = text("city").nullable()
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}