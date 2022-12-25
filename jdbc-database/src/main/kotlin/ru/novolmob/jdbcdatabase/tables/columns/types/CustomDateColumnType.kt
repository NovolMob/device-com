package ru.novolmob.jdbcdatabase.tables.columns.types

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import ru.novolmob.core.models.Dated
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.time.ZoneId
import java.util.*

class CustomDateColumnType<T: Dated>(
    val constructor: (LocalDate) -> T
): IColumnType<T> {
    override val databaseType: String = DatabaseVocabulary.DATE

    override fun fromDbType(db: Any): T =
        when (db) {
            is Date -> constructor(db.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toKotlinLocalDate())
            is java.sql.Date -> constructor(db.toLocalDate().toKotlinLocalDate())
            is LocalDateTime -> constructor(db.date)
            is LocalDate -> constructor(db)
            is java.time.LocalDateTime -> constructor(db.toLocalDate().toKotlinLocalDate())
            is java.time.LocalDate -> constructor(db.toKotlinLocalDate())
            else -> throw Exception("$db is not Date!")
        }

    override fun toDbType(value: T): Any = value.date
}