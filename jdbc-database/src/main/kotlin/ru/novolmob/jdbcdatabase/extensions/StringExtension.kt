package ru.novolmob.jdbcdatabase.extensions

object StringExtension {

    fun String.ifNotInEnd(end: String): String = if (endsWith(end)) this else this + end

}