package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.utils.StringChecking

@JvmInline
@Serializable
value class Email(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Email = Email(string.plus(any))

    companion object: StringChecking() {
        override val regex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")

        fun String.email(): Email = Email(this)
    }
}