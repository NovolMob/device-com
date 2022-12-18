package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Description(
    val string: String
) {
    override fun toString(): String = string
}