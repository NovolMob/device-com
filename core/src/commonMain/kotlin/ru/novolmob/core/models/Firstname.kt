package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.utils.IStringChecking
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Firstname(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Firstname = Firstname(string.plus(any))

    companion object {
        fun String.firstname(): Firstname = Firstname(this)
    }
}

expect object FirstnameChecking: IStringChecking