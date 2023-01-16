package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.utils.IStringChecking
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Email(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Email = Email(string.plus(any))

    companion object {
        fun String.email(): Email = Email(this)
    }
}

expect object EmailChecking: IStringChecking
