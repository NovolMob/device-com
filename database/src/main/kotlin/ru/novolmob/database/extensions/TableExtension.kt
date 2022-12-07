package ru.novolmob.database.extensions

import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date
import ru.novolmob.database.models.PhoneNumber
import ru.novolmob.database.tables.MapColumnType
import ru.novolmob.database.tables.credentials.PhoneNumberColumnType

object TableExtension {
    fun Table.phoneNumber(name: String): Column<PhoneNumber> = registerColumn(name, PhoneNumberColumnType())

    fun Table.updateDate(name: String = "update_date"): Column<LocalDate> = date(name)

    fun Table.creationDate(name: String = "creation_date"): Column<LocalDate> = date(name)

    fun Table.language(name: String = "language"): Column<String> = varchar(name, 5)

    fun Table.map(name: String): Column<Map<String, String>> = registerColumn(name, MapColumnType())
}