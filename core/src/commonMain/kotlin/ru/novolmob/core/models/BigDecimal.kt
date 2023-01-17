package ru.novolmob.core.models

expect class BigDecimal: Number {
    override fun toByte(): Byte
    override fun toChar(): Char
    override fun toDouble(): Double
    override fun toFloat(): Float
    override fun toInt(): Int
    override fun toLong(): Long
    override fun toShort(): Short
    operator fun plus(bigDecimal: BigDecimal): BigDecimal
    operator fun minus(bigDecimal: BigDecimal): BigDecimal
    operator fun div(bigDecimal: BigDecimal): BigDecimal
    operator fun times(bigDecimal: BigDecimal): BigDecimal
    operator fun rem(bigDecimal: BigDecimal): BigDecimal
    operator fun unaryMinus(): BigDecimal
    fun abs(): BigDecimal
    fun toPlainString(): String
    override fun equals(other: Any?): Boolean

    companion object {
        val ZERO: BigDecimal
        fun Int.toBigDecimal(): BigDecimal
        fun Long.toBigDecimal(): BigDecimal
        fun Double.toBigDecimal(): BigDecimal
        fun Float.toBigDecimal(): BigDecimal
        fun String.toBigDecimal(): BigDecimal
        inline fun <T> Iterable<T>.sumOf(selector: (T) -> BigDecimal): BigDecimal
    }

}