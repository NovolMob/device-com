package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Code(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Code = Code(string.plus(any))

    companion object {
        fun String.code(): Code = Code(this)
    }
}