package ru.novolmob.core.models

import ru.novolmob.core.utils.IStringChecking
import kotlin.js.RegExp

actual object LastnameChecking : IStringChecking {
    val regex = RegExp("[a-zA-Zа-яА-Я]+")
    override fun matches(value: String): Boolean = regex.test(value)
}