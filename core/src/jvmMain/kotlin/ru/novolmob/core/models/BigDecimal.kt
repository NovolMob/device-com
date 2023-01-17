package ru.novolmob.core.models

actual class BigDecimal(
    val javaBigDecimal: java.math.BigDecimal
): Number() {
    actual override fun toByte(): Byte = javaBigDecimal.toByte()
    actual override fun toChar(): Char = javaBigDecimal.toChar()
    actual override fun toDouble(): Double = javaBigDecimal.toDouble()
    actual override fun toFloat(): Float = javaBigDecimal.toFloat()
    actual override fun toInt(): Int = javaBigDecimal.toInt()
    actual override fun toLong(): Long = javaBigDecimal.toLong()
    actual override fun toShort(): Short = javaBigDecimal.toShort()

    actual operator fun plus(bigDecimal: BigDecimal): BigDecimal = BigDecimal(javaBigDecimal + bigDecimal.javaBigDecimal)
    actual operator fun minus(bigDecimal: BigDecimal): BigDecimal = BigDecimal(javaBigDecimal - bigDecimal.javaBigDecimal)
    actual operator fun div(bigDecimal: BigDecimal): BigDecimal = BigDecimal(javaBigDecimal / bigDecimal.javaBigDecimal)
    actual operator fun times(bigDecimal: BigDecimal): BigDecimal = BigDecimal(javaBigDecimal * bigDecimal.javaBigDecimal)
    actual operator fun rem(bigDecimal: BigDecimal): BigDecimal = BigDecimal(javaBigDecimal % bigDecimal.javaBigDecimal)

    actual operator fun unaryMinus(): BigDecimal = BigDecimal(javaBigDecimal.unaryMinus())
    actual fun abs(): BigDecimal = BigDecimal(javaBigDecimal.abs())

    actual fun toPlainString(): String = javaBigDecimal.toPlainString()
    actual override fun equals(other: Any?): Boolean {
        if (null == other)
            return false
        return if (other is BigDecimal) {
            javaBigDecimal == other.javaBigDecimal
        } else if (other is java.math.BigDecimal) {
            javaBigDecimal == other
        } else false
    }

    actual companion object {
        actual val ZERO: BigDecimal
            get() = BigDecimal(java.math.BigDecimal.ZERO)

        actual fun Int.toBigDecimal(): BigDecimal = BigDecimal(java.math.BigDecimal.valueOf(toLong()))
        actual fun Long.toBigDecimal(): BigDecimal = BigDecimal(java.math.BigDecimal.valueOf(this))
        actual fun Double.toBigDecimal(): BigDecimal = BigDecimal(java.math.BigDecimal.valueOf(this))
        actual fun Float.toBigDecimal(): BigDecimal = BigDecimal(java.math.BigDecimal.valueOf(toDouble()))
        actual fun String.toBigDecimal(): BigDecimal = BigDecimal(java.math.BigDecimal(this))
        fun java.math.BigDecimal.toBigDecimal(): BigDecimal = BigDecimal(this)
        actual inline fun <T> Iterable<T>.sumOf(selector: (T) -> BigDecimal): BigDecimal {
            var sum = ZERO
            for (element in this) {
                sum += selector(element)
            }
            return sum
        }
    }

}