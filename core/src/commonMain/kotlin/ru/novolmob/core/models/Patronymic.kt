package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.utils.StringChecking
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Patronymic(
    val string: String
) {
    override fun toString(): String = string

    operator fun plus(any: Any?): Patronymic = Patronymic(string.plus(any))

    companion object: StringChecking() {
        override val regex = Regex("[a-zA-Zа-яА-Я]+")

        fun String.patronymic(): Patronymic = Patronymic(this)
    }
}