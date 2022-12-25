package ru.novolmob.jdbcdatabase.extensions

object StringExtension {

    fun String.ifNotInEnd(end: String): String = if (endsWith(end)) this else this + end
    fun String.clearFirstAndLastQuotes(): String =
        (if (first() == '"') drop(1) else this).let {
            if (last() == '"') dropLast(1) else this
        }

}