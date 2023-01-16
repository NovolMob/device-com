package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.utils.IStringChecking
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Password(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Password = Password(string.plus(any))

    companion object {
        fun String.password(): Password = Password(this)
    }
}

expect object PasswordChecking: IStringChecking