package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.BigDecimalSerializer
import java.math.BigDecimal

@JvmInline
@Serializable
value class Price(
    @Serializable(with = BigDecimalSerializer::class)
    val bigDecimal: BigDecimal
): Numerical {
    override val number: Number
        get() = bigDecimal

    override fun toString(): String = bigDecimal.toString()

    operator fun plus(int: Int): Price = Price(bigDecimal + int.toBigDecimal())
    operator fun plus(price: Price): Price = Price(bigDecimal + price.bigDecimal)
    operator fun unaryPlus(): Price = Price(bigDecimal.abs())
    operator fun minus(int: Int): Price = Price(bigDecimal - int.toBigDecimal())
    operator fun minus(price: Price): Price = Price(bigDecimal - price.bigDecimal)
    operator fun unaryMinus(): Price = Price(-bigDecimal)
    operator fun times(int: Int): Price = Price(bigDecimal * int.toBigDecimal())
    operator fun times(price: Price): Price = Price(bigDecimal * price.bigDecimal)
    operator fun div(int: Int): Price = Price(bigDecimal / int.toBigDecimal())
    operator fun div(price: Price): Price = Price(bigDecimal / price.bigDecimal)
    operator fun rem(int: Int): Price = Price(bigDecimal % int.toBigDecimal())
    operator fun rem(price: Price): Price = Price(bigDecimal % price.bigDecimal)
    operator fun inc(): Price = Price(bigDecimal + 1.toBigDecimal())
    operator fun dec(): Price = Price(bigDecimal - 1.toBigDecimal())

    companion object {
        val ZERO: Price
            get() = Price(BigDecimal.ZERO)

        fun BigDecimal.price(): Price = Price(this)
        fun Int.price(): Price = Price(this.toBigDecimal())
        fun Double.price(): Price = Price(this.toBigDecimal())
    }
}