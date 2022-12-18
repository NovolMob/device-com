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
}