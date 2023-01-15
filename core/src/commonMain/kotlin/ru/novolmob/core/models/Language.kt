package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Language(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Language = Language(string.plus(any))

    companion object {
        fun String.language(): Language = Language(this)
    }
}