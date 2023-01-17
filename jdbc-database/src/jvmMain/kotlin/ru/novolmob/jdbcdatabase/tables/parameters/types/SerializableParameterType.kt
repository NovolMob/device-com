package ru.novolmob.jdbcdatabase.tables.parameters.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.sql.Types

class SerializableParameterType<T>(
    private val serializer: KSerializer<T>
): IParameterType<T> {
    private val json = Json
    override val databaseType: String = DatabaseVocabulary.TEXT
    override val databaseTypeId: Int = Types.VARCHAR

    override fun fromDbType(db: Any): T =
        when (db) {
            is String -> json.decodeFromString(serializer, "$db")
            else -> throw Exception("$db cannot be serialized!")
        }

    override fun toDbType(value: T): Any = json.encodeToString(serializer, value)
}