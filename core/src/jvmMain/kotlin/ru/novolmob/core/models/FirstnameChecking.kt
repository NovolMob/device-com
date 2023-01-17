package ru.novolmob.core.models

import ru.novolmob.core.utils.IStringChecking

actual object FirstnameChecking : IStringChecking {
    private val regex = Regex("[a-zA-Zа-яА-Я]+")
    override fun matches(value: String): Boolean = regex.matches(value)
}