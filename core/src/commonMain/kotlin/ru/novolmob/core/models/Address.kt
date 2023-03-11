package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Address(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Address = Address(string.plus(any))

    companion object {
        fun String.address(): Address = Address(this)
    }
}