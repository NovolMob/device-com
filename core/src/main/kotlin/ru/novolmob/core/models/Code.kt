package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Code(
    val string: String
) {
    override fun toString(): String = string
}