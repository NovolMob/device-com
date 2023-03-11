package ru.novolmob.jdbcdatabase.tables.parameters.types

import ru.novolmob.core.models.Numerical
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.math.BigDecimal
import java.sql.Types

class CustomIntegerParameterType<T: Numerical>(
    val constructor: (Int) -> T
): IParameterType<T> {
    override val databaseType: String
        get() = DatabaseVocabulary.INT
    override val databaseTypeId: Int = Types.INTEGER

    override fun fromDbType(db: Any): T =
        when (db) {
            is BigDecimal -> constructor(db.toInt())
            is Double -> constructor(db.toInt())
            is Float -> constructor(db.toInt())
            is Int -> constructor(db)
            else -> throw Exception("$db is not int!")
        }

    override fun toDbType(value: T): Any = value.number
}