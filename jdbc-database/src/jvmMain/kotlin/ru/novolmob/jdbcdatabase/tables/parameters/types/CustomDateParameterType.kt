@file:OptIn(InternalAPI::class)

package ru.novolmob.jdbcdatabase.tables.parameters.types

import io.ktor.server.util.*
import io.ktor.util.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import ru.novolmob.core.models.Dated
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.sql.Types
import java.util.*

class CustomDateParameterType<T: Dated>(
    val constructor: (LocalDate) -> T
): IParameterType<T> {
    override val databaseType: String = DatabaseVocabulary.DATE
    override val databaseTypeId: Int = Types.DATE

    override fun fromDbType(db: Any): T =
        when (db) {
            is java.sql.Date -> constructor(LocalDate.parse(db.toString()))
            is Date -> constructor(db.toLocalDateTime().toLocalDate().toKotlinLocalDate())
            is LocalDateTime -> constructor(db.date)
            is LocalDate -> constructor(db)
            is java.time.LocalDateTime -> constructor(db.toLocalDate().toKotlinLocalDate())
            is java.time.LocalDate -> constructor(db.toKotlinLocalDate())
            else -> throw Exception("$db is not Date!")
        }

    override fun toDbType(value: T): Any = value.date
}