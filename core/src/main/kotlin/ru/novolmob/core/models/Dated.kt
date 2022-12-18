package ru.novolmob.core.models

import kotlinx.datetime.LocalDateTime

interface Dated {
    val date: LocalDateTime
}