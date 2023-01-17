package ru.novolmob.core.models

import ru.novolmob.core.utils.IStringChecking

actual object EmailChecking : IStringChecking {
    private val regex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    override fun matches(value: String): Boolean = regex.matches(value)
}