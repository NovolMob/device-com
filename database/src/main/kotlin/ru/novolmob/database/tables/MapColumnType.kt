package ru.novolmob.database.tables

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ColumnType
import java.sql.Clob

class MapColumnType: ColumnType() {
    private val json = Json
    override fun sqlType(): String = "TEXT"

    override fun valueFromDB(value: Any): Any =
        when (value) {
            is Clob -> json.decodeFromString<Map<String, String>>(value.characterStream.readText())
            is ByteArray -> json.decodeFromString<Map<String, String>>(String(value))
            else -> value
        }

    override fun valueToDB(value: Any?): Any? =
        when(value) {
            is Map<*, *> -> json.encodeToString(value)
            else -> value
        }

}