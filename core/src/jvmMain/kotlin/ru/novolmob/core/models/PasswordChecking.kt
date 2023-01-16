package ru.novolmob.core.models

import ru.novolmob.core.utils.IStringChecking

actual object PasswordChecking : IStringChecking {
    private val regex = Regex("\\w+\\d+")
    override fun matches(value: String): Boolean = regex.matches(value)
}