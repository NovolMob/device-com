package ru.novolmob.core.models

import ru.novolmob.core.utils.IStringChecking
import kotlin.js.RegExp

actual object PasswordChecking : IStringChecking {
    val regex = RegExp("\\w+\\d+")
    override fun matches(value: String): Boolean = regex.test(value)
}