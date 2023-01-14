package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Amount(
    val int: Int
): Numerical {
    override val number: Number
        get() = int
    override fun toString(): String = int.toString()

    operator fun plus(int: Int): Amount = Amount(this.int + int)
    operator fun plus(amount: Amount): Amount = Amount(int + amount.int)
    operator fun unaryPlus(): Amount = Amount(+int)
    operator fun minus(int: Int): Amount = Amount(this.int - int)
    operator fun minus(amount: Amount): Amount = Amount(this.int - amount.int)
    operator fun unaryMinus(): Amount = Amount(-int)
    operator fun times(int: Int): Amount = Amount(this.int * int)
    operator fun times(amount: Amount): Amount = Amount(this.int * amount.int)
    operator fun div(int: Int): Amount = Amount(this.int / int)
    operator fun div(amount: Amount): Amount = Amount(this.int / amount.int)
    operator fun rem(int: Int): Amount = Amount(this.int % int)
    operator fun rem(amount: Amount): Amount = Amount(this.int % amount.int)
    operator fun inc(): Amount = Amount(int + 1)
    operator fun dec(): Amount = Amount(int - 1)

    companion object {
        val ZERO: Amount
            get() = Amount(0)

        fun Int.amount(): Amount = Amount(this)
        fun Double.amount(): Amount = Amount(this.toInt())
    }
}