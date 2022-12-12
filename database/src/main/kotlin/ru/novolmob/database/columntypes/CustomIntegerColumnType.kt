package ru.novolmob.database.columntypes

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.vendors.currentDialect
import ru.novolmob.database.models.Numerical
import java.sql.ResultSet

class CustomIntegerColumnType<T: Numerical>(
    val constructor: (Int) -> T
): ColumnType() {
    override fun sqlType(): String = currentDialect.dataTypeProvider.integerType()
    override fun valueFromDB(value: Any): T = when (value) {
        is Int -> value
        is Number -> value.toInt()
        is String -> value.toInt()
        else -> error("Unexpected value of type Int: $value of ${value::class.qualifiedName}")
    }.let(constructor)

    override fun readObject(rs: ResultSet, index: Int): Any? {
        return rs.getInt(index).let(constructor)
    }

    override fun notNullValueToDB(value: Any): Any =
        when (value) {
            is Numerical -> value.number
            else -> value
        }
}