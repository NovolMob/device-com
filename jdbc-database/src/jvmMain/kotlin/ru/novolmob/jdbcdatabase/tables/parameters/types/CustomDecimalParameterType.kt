package ru.novolmob.jdbcdatabase.tables.parameters.types

import ru.novolmob.core.models.BigDecimal
import ru.novolmob.core.models.BigDecimal.Companion.toBigDecimal
import ru.novolmob.core.models.Numerical
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.sql.Types

class CustomDecimalParameterType<T: Numerical>(
    val constructor: (BigDecimal) -> T,
    val precision: Int,
    val scale: Int
): IParameterType<T> {
    override val databaseType: String
        get() = DatabaseVocabulary.decimal(precision, scale)
    override val databaseTypeId: Int = Types.DECIMAL

    override fun fromDbType(db: Any): T =
        when (db) {
            is BigDecimal -> constructor(db)
            is java.math.BigDecimal -> constructor(db.toBigDecimal())
            is Double -> constructor(db.toBigDecimal())
            is Float -> constructor(db.toBigDecimal())
            is Int -> constructor(db.toBigDecimal())
            else -> throw Exception("$db is not decimal!")
        }

    override fun toDbType(value: T): Any = value.number
}