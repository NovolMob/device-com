package ru.novolmob.jdbcdatabase.tables.columns.types

import ru.novolmob.core.models.ids.UUIDable
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.util.*

class CustomUuidColumnType<T: UUIDable>(
    val constructor: (UUID) -> T
): IColumnType<T> {
    override val databaseType: String = DatabaseVocabulary.UUID

    override fun fromDbType(db: Any): T =
        when (db) {
            is UUID -> constructor(db)
            else -> throw Exception("$db is not Date!")
        }

    override fun toDbType(value: T): Any = value.uuid
}