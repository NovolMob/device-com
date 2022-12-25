package ru.novolmob.jdbcdatabase.tables.columns.types

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import ru.novolmob.core.models.DateTimed
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.sql.Timestamp
import java.time.ZoneId
import java.util.*

class CustomDateTimeColumnType<T: DateTimed>(
    val constructor: (LocalDateTime) -> T
): IColumnType<T> {
    override val databaseType: String = DatabaseVocabulary.TIMESTAMP

    override fun fromDbType(db: Any): T =
        when (db) {
            is Timestamp -> constructor(db.toLocalDateTime().toKotlinLocalDateTime())
            is Date -> constructor(db.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toKotlinLocalDateTime())
            is java.sql.Date -> constructor(db.toLocalDate().atStartOfDay().toKotlinLocalDateTime())
            is LocalDateTime -> constructor(db)
            is LocalDate -> constructor(db.atStartOfDayIn(TimeZone.currentSystemDefault()).toLocalDateTime(TimeZone.currentSystemDefault()))
            is java.time.LocalDateTime -> constructor(db.toKotlinLocalDateTime())
            is java.time.LocalDate -> constructor(db.atStartOfDay().toKotlinLocalDateTime())
            else -> throw Exception("$db is not Date!")
        }

    override fun toDbType(value: T): Any = value.dateTime
}