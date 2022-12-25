package ru.novolmob.core.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.novolmob.core.extensions.LocalDateTimeExtension.now

@JvmInline
@Serializable
value class CreationTime(
    override val dateTime: LocalDateTime
): Comparable<CreationTime>, DateTimed {
    override fun compareTo(other: CreationTime): Int = dateTime.compareTo(other.dateTime)

    override fun toString(): String = dateTime.toString()
    companion object {
        fun now(): CreationTime = CreationTime(LocalDateTime.now())
    }
}