package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.utils.IStringChecking
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Lastname(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Lastname = Lastname(string.plus(any))

    companion object {
        fun String.lastname(): Lastname = Lastname(this)
    }
}

expect object LastnameChecking: IStringChecking