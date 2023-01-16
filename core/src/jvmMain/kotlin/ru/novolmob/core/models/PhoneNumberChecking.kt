package ru.novolmob.core.models

import ru.novolmob.core.utils.IStringChecking

actual object PhoneNumberChecking : IStringChecking {
    private val regex = Regex("\\d{11}")
    override fun matches(value: String): Boolean = regex.matches(value)
}