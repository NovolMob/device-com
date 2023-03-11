package ru.novolmob.jdbcdatabase.tables.parameters.types

import ru.novolmob.core.models.ids.UUID
import ru.novolmob.core.models.ids.UUID.Companion.toUUID
import ru.novolmob.core.models.ids.UUIDable
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.sql.Types

class CustomUuidParameterType<T: UUIDable>(
    val constructor: (UUID) -> T
): IParameterType<T> {
    override val databaseType: String = DatabaseVocabulary.UUID
    override val databaseTypeId: Int = Types.JAVA_OBJECT

    override fun fromDbType(db: Any): T =
        when (db) {
            is UUID -> constructor(db)
            is java.util.UUID -> constructor(db.toUUID())
            else -> throw Exception("$db is not Date!")
        }

    override fun toDbType(value: T): Any = value.uuid
}