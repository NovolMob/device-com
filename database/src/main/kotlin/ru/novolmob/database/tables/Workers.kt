package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.date
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.updateDate

object Workers: UUIDTable() {
    val point = reference("point_id", Points)
    val firstname = text("firstname")
    val lastname = text("lastname")
    val patronymic = text("patronymic").nullable()
    val birthday = date("birthday").nullable()
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}