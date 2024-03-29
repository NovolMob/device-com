package ru.novolmob.exposeddatabase.columntypes

import org.jetbrains.exposed.sql.ColumnType
import ru.novolmob.core.models.BigDecimal
import ru.novolmob.core.models.Numerical
import java.math.RoundingMode
import java.sql.ResultSet

class CustomDecimalColumnType<T: Numerical>(
    val constructor: (BigDecimal) -> T,
    val precision: Int,
    val scale: Int
): ColumnType() {
    override fun sqlType(): String = "DECIMAL($precision, $scale)"

    override fun readObject(rs: ResultSet, index: Int): Any? {
        return rs.getBigDecimal(index)?.let { constructor(BigDecimal(it)) }
    }

    override fun nonNullValueToString(value: Any): String = when(value) {
        is Numerical -> super.nonNullValueToString(value.number.toDouble().toBigDecimal())
        else -> super.nonNullValueToString(value)
    }

    override fun notNullValueToDB(value: Any): Any = when(value) {
        is Numerical -> super.notNullValueToDB(value.number.toDouble().toBigDecimal())
        else -> super.notNullValueToDB(value)
    }

    override fun valueFromDB(value: Any): T = when (value) {
        is Numerical -> value.number.toDouble().toBigDecimal()
        is BigDecimal -> value.javaBigDecimal
        is java.math.BigDecimal -> value
        is Double -> {
            if (value.isNaN()) {
                error("Unexpected value of type Double: NaN of ${value::class.qualifiedName}")
            } else {
                value.toBigDecimal()
            }
        }
        is Float -> {
            if (value.isNaN()) {
                error("Unexpected value of type Float: NaN of ${value::class.qualifiedName}")
            } else {
                value.toBigDecimal()
            }
        }
        is Long -> value.toBigDecimal()
        is Int -> value.toBigDecimal()
        else -> error("Unexpected value of type Decimal: $value of ${value::class.qualifiedName}")
    }.setScale(scale, RoundingMode.HALF_EVEN).let { constructor(BigDecimal(it)) }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as CustomDecimalColumnType<*>

        if (precision != other.precision) return false
        if (scale != other.scale) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + precision
        result = 31 * result + scale
        return result
    }
}