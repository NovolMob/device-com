package ru.novolmob.core.utils

abstract class StringChecking {
    abstract val regex: Regex

    fun matches(value: String): Boolean = regex.matches(value)
}