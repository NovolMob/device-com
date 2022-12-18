package ru.novolmob.database.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Amount(
    val int: Int
): Numerical {
    override val number: Number
        get() = int
    override fun toString(): String = int.toString()
}