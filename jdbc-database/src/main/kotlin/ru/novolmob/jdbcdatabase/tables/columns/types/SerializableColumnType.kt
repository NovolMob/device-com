package ru.novolmob.jdbcdatabase.tables.columns.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary

class SerializableColumnType<T>(
    private val serializer: KSerializer<T>
): IColumnType<T> {
    private val json = Json
    override val databaseType: String = DatabaseVocabulary.TEXT

    override fun fromDbType(db: Any): T =
        when (db) {
            is String -> json.decodeFromString(serializer, "\"$db\"")
            else -> throw Exception("$db cannot be serialized!")
        }

    override fun toDbType(value: T): Any = json.encodeToString(serializer, value).drop(1).dropLast(1)
}