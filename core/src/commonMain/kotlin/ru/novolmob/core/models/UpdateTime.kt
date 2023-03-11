package ru.novolmob.core.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.serialization.Serializable
import ru.novolmob.core.extensions.LocalDateTimeExtension.now
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class UpdateTime(
    override val dateTime: LocalDateTime
): Comparable<UpdateTime>, DateTimed {

    constructor(year: Int, monthNumber: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int, nanosecond: Int):
            this(LocalDateTime(year, monthNumber, dayOfMonth, hour, minute, second, nanosecond))

    constructor(year: Int, month: Month, dayOfMonth: Int, hour: Int, minute: Int, second: Int, nanosecond: Int):
            this(LocalDateTime(year, month, dayOfMonth, hour, minute, second, nanosecond))

    constructor(date: LocalDate, time: LocalTime): this(LocalDateTime(date, time))

    override fun compareTo(other: UpdateTime): Int = dateTime.compareTo(other.dateTime)

    override fun toString(): String = dateTime.toString()
    companion object {
        fun now(): UpdateTime = UpdateTime(LocalDateTime.now())
        fun parse(isoString: String): UpdateTime = UpdateTime(LocalDateTime.parse(isoString))
    }
}