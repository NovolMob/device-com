package ru.novolmob.database.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class UpdateDate(
    override val date: LocalDateTime
): Dated {
    override fun toString(): String = date.toString()
}