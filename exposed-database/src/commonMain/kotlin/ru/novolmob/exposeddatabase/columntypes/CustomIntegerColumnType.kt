package ru.novolmob.exposeddatabase.columntypes

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.vendors.currentDialect
import ru.novolmob.core.models.Numerical
import java.sql.ResultSet

class CustomIntegerColumnType<T: Numerical>(
    val constructor: (Int) -> T
): ColumnType() {
    override fun sqlType(): String = currentDialect.dataTypeProvider.integerType()

    override fun nonNullValueToString(value: Any): String = when(value) {
        is Numerical -> super.nonNullValueToString(value.number.toInt())
        else -> super.nonNullValueToString(value)
    }

    override fun valueFromDB(value: Any): T = when (value) {
        is Numerical -> value.number.toInt()
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
            is Numerical -> super.notNullValueToDB(value.number.toInt())
            else -> super.notNullValueToDB(value)
        }
}