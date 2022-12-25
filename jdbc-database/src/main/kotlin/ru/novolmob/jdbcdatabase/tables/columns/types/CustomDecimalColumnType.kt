package ru.novolmob.jdbcdatabase.tables.columns.types

import ru.novolmob.core.models.Numerical
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.math.BigDecimal

class CustomDecimalColumnType<T: Numerical>(
    val constructor: (BigDecimal) -> T,
    val precision: Int,
    val scale: Int
): IColumnType<T> {
    override val databaseType: String
        get() = DatabaseVocabulary.decimal(precision, scale)

    override fun fromDbType(db: Any): T =
        when (db) {
            is BigDecimal -> constructor(db)
            is Double -> constructor(db.toBigDecimal())
            is Float -> constructor(db.toBigDecimal())
            is Int -> constructor(db.toBigDecimal())
            else -> throw Exception("$db is not decimal!")
        }

    override fun toDbType(value: T): Any = value.number
}