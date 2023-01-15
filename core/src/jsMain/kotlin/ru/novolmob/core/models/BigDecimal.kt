package ru.novolmob.core.models

@JsModule("js-big-decimal")
external class BigDecimalJs(string: String) {
    val value: String

    operator fun plus(other: BigDecimalJs): BigDecimalJs
    fun compareTo(other: BigDecimalJs): Int
    fun add(other: BigDecimalJs): BigDecimalJs
    fun subtract(other: BigDecimalJs): BigDecimalJs
    fun multiply(other: BigDecimalJs): BigDecimalJs
    fun divide(other: BigDecimalJs): BigDecimalJs
    fun modulus(other: BigDecimalJs): BigDecimalJs
    fun negate(): BigDecimalJs
    fun abs(): BigDecimalJs
    fun getPrettyValue(digits: Int?, separator: String?): String
}

actual class BigDecimal(private val jsBigDecimal: BigDecimalJs): Number() {
    actual override fun toByte(): Byte = jsBigDecimal.value.toDouble().toInt().toByte()
    actual override fun toChar(): Char = jsBigDecimal.value.toDouble().toInt().toChar()
    actual override fun toDouble(): Double = jsBigDecimal.value.toDouble()
    actual override fun toFloat(): Float = jsBigDecimal.value.toFloat()
    actual override fun toInt(): Int = jsBigDecimal.value.toDouble().toInt()
    actual override fun toLong(): Long = jsBigDecimal.value.toDouble().toLong()
    actual override fun toShort(): Short = jsBigDecimal.value.toDouble().toInt().toShort()

    actual operator fun plus(bigDecimal: BigDecimal): BigDecimal = BigDecimal(jsBigDecimal.add(bigDecimal.jsBigDecimal))
    actual operator fun minus(bigDecimal: BigDecimal): BigDecimal = BigDecimal(jsBigDecimal.subtract(bigDecimal.jsBigDecimal))
    actual operator fun div(bigDecimal: BigDecimal): BigDecimal = BigDecimal(jsBigDecimal.divide(bigDecimal.jsBigDecimal))
    actual operator fun times(bigDecimal: BigDecimal): BigDecimal = BigDecimal(jsBigDecimal.multiply(bigDecimal.jsBigDecimal))
    actual operator fun rem(bigDecimal: BigDecimal): BigDecimal = BigDecimal(jsBigDecimal.modulus(bigDecimal.jsBigDecimal))

    actual operator fun unaryMinus(): BigDecimal = BigDecimal(jsBigDecimal.negate())
    actual fun abs(): BigDecimal = BigDecimal(jsBigDecimal.abs())

    actual fun toPlainString(): String = jsBigDecimal.value
    override fun toString(): String = toPlainString()

    actual companion object {
        actual val ZERO: BigDecimal = "0.0".toBigDecimal()

        actual fun Int.toBigDecimal(): BigDecimal = BigDecimal(BigDecimalJs(toString()))
        actual fun Long.toBigDecimal(): BigDecimal = BigDecimal(BigDecimalJs(toString()))
        actual fun Double.toBigDecimal(): BigDecimal = BigDecimal(BigDecimalJs(toString()))
        actual fun Float.toBigDecimal(): BigDecimal = BigDecimal(BigDecimalJs(toString()))
        actual fun String.toBigDecimal(): BigDecimal = BigDecimal(BigDecimalJs(this))
        actual inline fun <T> Iterable<T>.sumOf(selector: (T) -> BigDecimal): BigDecimal {
            var sum = ZERO
            for (element in this) {
                sum += selector(element)
            }
            return sum
        }
    }

}