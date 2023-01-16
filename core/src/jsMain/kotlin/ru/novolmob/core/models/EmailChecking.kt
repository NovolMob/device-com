package ru.novolmob.core.models

import ru.novolmob.core.utils.IStringChecking
import kotlin.js.RegExp

actual object EmailChecking : IStringChecking {
    val regex = RegExp("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    
    override fun matches(value: String): Boolean = regex.test(value)
}