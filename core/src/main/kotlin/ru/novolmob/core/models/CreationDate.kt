package ru.novolmob.core.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.novolmob.core.extensions.LocalDateTimeExtension.now

@JvmInline
@Serializable
value class CreationDate(
    override val date: LocalDateTime
): Comparable<CreationDate>, Dated {
    override fun compareTo(other: CreationDate): Int = date.compareTo(other.date)

    override fun toString(): String = date.toString()
    companion object {
        fun now(): CreationDate = CreationDate(LocalDateTime.now())
    }
}