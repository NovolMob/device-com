package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Password(
    val string: String
) {
    override fun toString(): String = string
}