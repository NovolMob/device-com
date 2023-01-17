package ru.novolmob.core.models

import ru.novolmob.core.utils.IStringChecking

actual object LastnameChecking : IStringChecking {
    private val regex = Regex("[a-zA-Zа-яА-Я]+")
    override fun matches(value: String): Boolean = regex.matches(value)
}