package ru.novolmob.exposeddatabase.columntypes

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.TextColumnType
import java.sql.Clob
import kotlin.reflect.KClass

class CustomTextColumnType<T: Any>(
    val klass: KClass<T>,
    val serializer: KSerializer<T>,
    val json: Json = Json,
    collate: String? = null,
    eagerLoading: Boolean = false
) : TextColumnType(collate, eagerLoading) {
    override fun valueFromDB(value: Any): Any = when (value) {
        is Clob -> json.decodeFromString(serializer, value.characterStream.readText())
        is ByteArray -> json.decodeFromString(serializer, String(value))
        else -> value
    }

    private fun valueToText(value: Any): String =
        if (klass.isInstance(value)) json.encodeToString(serializer, value as T) else value.toString()
    override fun notNullValueToDB(value: Any): Any = valueToText(value)
    override fun nonNullValueToString(value: Any): String = valueToText(value)
}