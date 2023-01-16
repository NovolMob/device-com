package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.utils.IStringChecking
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Patronymic(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Patronymic = Patronymic(string.plus(any))

    companion object {
        fun String.patronymic(): Patronymic = Patronymic(this)
    }
}

expect object PatronymicChecking: IStringChecking