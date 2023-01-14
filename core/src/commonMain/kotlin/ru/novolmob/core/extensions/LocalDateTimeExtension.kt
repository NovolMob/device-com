package ru.novolmob.core.extensions

import kotlinx.datetime.*

object LocalDateTimeExtension {
    fun LocalDateTime.Companion.now(): LocalDateTime =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    fun LocalDate.Companion.now(): LocalDate =
        LocalDateTime.now().date
}