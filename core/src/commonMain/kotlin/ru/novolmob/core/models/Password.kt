package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.utils.StringChecking

@JvmInline
@Serializable
value class Password(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Password = Password(string.plus(any))

    companion object: StringChecking() {
        override val regex = Regex("\\w+\\d+")

        fun String.password(): Password = Password(this)
    }
}