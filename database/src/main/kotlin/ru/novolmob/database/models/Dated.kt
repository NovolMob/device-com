package ru.novolmob.database.models

import kotlinx.datetime.LocalDateTime

interface Dated {
    val date: LocalDateTime
}