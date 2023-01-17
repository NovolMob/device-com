package ru.novolmob.core.models

import ru.novolmob.core.utils.IStringChecking
import kotlin.js.RegExp

actual object PhoneNumberChecking : IStringChecking {
    val regex = RegExp("\\d{11}")
    override fun matches(value: String): Boolean = regex.test(value)
}