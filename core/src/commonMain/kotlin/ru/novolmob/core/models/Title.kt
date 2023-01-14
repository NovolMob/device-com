package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Title(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Title = Title(string.plus(any))

    companion object {
        fun String.description(): Title = Title(this)
    }
}