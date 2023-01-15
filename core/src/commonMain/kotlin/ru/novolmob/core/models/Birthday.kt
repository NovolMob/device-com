package ru.novolmob.core.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Birthday(
    override val date: LocalDate
): Dated {

    constructor(year: Int, monthNumber: Int, dayOfMonth: Int): this(LocalDate(year, monthNumber, dayOfMonth))
    constructor(year: Int, month: Month, dayOfMonth: Int): this(LocalDate(year, month, dayOfMonth))

    override fun toString(): String = date.toString()

    companion object {
        fun LocalDate.birthday(): Birthday = Birthday(this)
        fun parse(isoString: String): Birthday = Birthday(LocalDate.parse(isoString))
        fun fromEpochDays(epochDays: Int): Birthday = Birthday(LocalDate.fromEpochDays(epochDays))
    }
}