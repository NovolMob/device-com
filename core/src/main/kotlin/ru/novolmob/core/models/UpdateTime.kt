package ru.novolmob.core.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.novolmob.core.extensions.LocalDateTimeExtension.now

@JvmInline
@Serializable
value class UpdateTime(
    override val dateTime: LocalDateTime
): Comparable<UpdateTime>, DateTimed {
    override fun compareTo(other: UpdateTime): Int = dateTime.compareTo(other.dateTime)

    override fun toString(): String = dateTime.toString()
    companion object {
        fun now(): UpdateTime = UpdateTime(LocalDateTime.now())
    }
}