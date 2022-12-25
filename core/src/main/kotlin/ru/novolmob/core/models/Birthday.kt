package ru.novolmob.core.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Birthday(
    override val date: LocalDate
): Dated {
    override fun toString(): String = date.toString()
}