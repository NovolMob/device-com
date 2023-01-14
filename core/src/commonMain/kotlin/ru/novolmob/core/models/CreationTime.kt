package ru.novolmob.core.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.serialization.Serializable
import ru.novolmob.core.extensions.LocalDateTimeExtension.now

@JvmInline
@Serializable
value class CreationTime(
    override val dateTime: LocalDateTime
): Comparable<CreationTime>, DateTimed {

    constructor(year: Int, monthNumber: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int, nanosecond: Int):
            this(LocalDateTime(year, monthNumber, dayOfMonth, hour, minute, second, nanosecond))

    constructor(year: Int, month: Month, dayOfMonth: Int, hour: Int, minute: Int, second: Int, nanosecond: Int):
            this(LocalDateTime(year, month, dayOfMonth, hour, minute, second, nanosecond))

    constructor(date: LocalDate, time: LocalTime): this(LocalDateTime(date, time))

    override fun compareTo(other: CreationTime): Int = dateTime.compareTo(other.dateTime)

    override fun toString(): String = dateTime.toString()
    companion object {
        fun now(): CreationTime = CreationTime(LocalDateTime.now())
        fun parse(isoString: String): CreationTime = CreationTime(LocalDateTime.parse(isoString))
    }
}