package ru.novolmob.core.models

import kotlinx.datetime.LocalDate

interface Dated {
    val date: LocalDate
}