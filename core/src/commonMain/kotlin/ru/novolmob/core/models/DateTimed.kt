package ru.novolmob.core.models

import kotlinx.datetime.LocalDateTime

interface DateTimed {
    val dateTime: LocalDateTime
}