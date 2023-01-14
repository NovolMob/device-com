package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.utils.StringChecking

@JvmInline
@Serializable
value class Firstname(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Firstname = Firstname(string.plus(any))

    companion object: StringChecking() {
        override val regex = Regex("[a-zA-Zа-яА-Я]+")

        fun String.firstname(): Firstname = Firstname(this)
    }
}