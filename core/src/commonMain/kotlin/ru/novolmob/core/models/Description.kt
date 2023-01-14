package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Description(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Description = Description(string.plus(any))

    companion object {
        fun String.description(): Description = Description(this)
    }
}