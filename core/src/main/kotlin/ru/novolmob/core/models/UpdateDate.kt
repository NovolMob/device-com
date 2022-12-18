package ru.novolmob.core.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class UpdateDate(
    override val date: LocalDateTime
): Comparable<UpdateDate>, Dated {
    override fun compareTo(other: UpdateDate): Int = date.compareTo(other.date)

    override fun toString(): String = date.toString()
    companion object {
        fun now(): UpdateDate = UpdateDate(LocalDateTime.now())
    }
}