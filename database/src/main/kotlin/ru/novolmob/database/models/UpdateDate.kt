package ru.novolmob.database.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.novolmob.database.extensions.LocalDateTimeExtension.now

@JvmInline
@Serializable
value class UpdateDate(
    override val date: LocalDateTime
): Dated {
    override fun toString(): String = date.toString()
    companion object {
        fun now(): UpdateDate = UpdateDate(LocalDateTime.now())
    }
}