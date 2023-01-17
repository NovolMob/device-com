package ru.novolmob.jdbcdatabase.extensions

import java.util.*

object DateExtension {
    fun Date.toSql(): java.sql.Date = java.sql.Date(time)
}